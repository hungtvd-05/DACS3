package com.app_computer_ecom.dack.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.cloudinary.Cloudinary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.set
import kotlin.toString

object ImageCloudinary {
    private fun Config(): Cloudinary {
        val config = HashMap<String, String>()
        config["cloud_name"] = "dcuzzoyoz"
        config["api_key"] = "812936661584879"
        config["api_secret"] = "57fCpc2Pqaj7VHWI2F3jo28WYM0"

        val cloudinary = Cloudinary(config)

        return cloudinary
    }

    suspend fun uploadImage(context: Context, btm: Bitmap): Result<String> =
        withContext(Dispatchers.IO) {

            return@withContext try {
                val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(file)
                btm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                val options = HashMap<String, Any>()
                options["resource_type"] = "auto"
                options["folder"] = "test"

                val uploadResult = Config().uploader().upload(file.absolutePath, options)
                val imageUrl = uploadResult["secure_url"] as String

                file.delete()

                Result.success(imageUrl)
            } catch (e: Exception) {
                Log.e("uploadImage", "Error uploading image", e)
                Result.failure(e)
            }

        }

    suspend fun deleteImage(path: String?) {
        val publicId = extractPublicId(path.toString())
        try {
            val result = Config().uploader().destroy(publicId, HashMap<String, Any>())
            val success = result["result"] == "ok"
            withContext(Dispatchers.Main) {
                if (success) {
                    Log.d("Cloudinary", "Xóa ảnh thành công: $publicId")
                } else {
                    Log.e("Cloudinary", "Xóa ảnh thất bại: ${result["result"]}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Log.e("Cloudinary", "Lỗi khi xóa ảnh: ${e.message}")
            }
        }
    }
}

fun extractPublicId(imageUrl: String): String {
    val regex = """.*/upload/v\d+/(.*)\.\w+$""".toRegex()
    val matchResult = regex.find(imageUrl)
    return matchResult?.groupValues?.get(1) ?: ""
}

fun buildAnnotatedText(text: String): AnnotatedString {
    val annotatedString = AnnotatedString.Builder(text)
    val regex = """(https?:\/\/\S+\.(jpg|png|jpeg|gif))""".toRegex()
    regex.findAll(text).forEach { match ->
        annotatedString.addStyle(
            style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
            start = match.range.first,
            end = match.range.last + 1
        )
        annotatedString.addStringAnnotation(
            tag = "URL",
            annotation = match.value,
            start = match.range.first,
            end = match.range.last + 1
        )
    }
    return annotatedString.toAnnotatedString()
}

fun extractImageUrls(text: String): List<String> {
    val regex = """(https?:\/\/\S+\.(jpg|png|jpeg|gif))""".toRegex()
    return regex.findAll(text).map { it.value }.toList()
}