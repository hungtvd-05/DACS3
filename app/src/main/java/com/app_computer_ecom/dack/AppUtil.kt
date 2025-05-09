package com.app_computer_ecom.dack

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppUtil {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    suspend fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val resolver = context.contentResolver
                val stream = resolver.openInputStream(uri)
                BitmapFactory.decodeStream(stream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


}