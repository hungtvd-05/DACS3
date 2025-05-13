package com.app_computer_ecom.dack

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.app_computer_ecom.dack.model.ProductInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

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

    suspend fun sendEmail(subject: String, body: String, recipient: String) {
        val email = "forestcineplex@gmail.com"
        val password = "dhfk dxvg yghq bloo"

        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(email, password)
            }
        })

        try {
            withContext(Dispatchers.IO) {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(email))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                    this.subject = subject

                    setContent(body, "text/html; charset=utf-8")
                }

                Transport.send(message)
                Log.d("EMAIL", "Email sent successfully $recipient")
            }
        } catch (e: Exception) {
            Log.d("EMAIL", "ail error: ${e.message}")
        }
    }

    fun convertStatusToString(status: Int): String {
        return when (status) {
            0 -> "Chờ xác nhận"
            1 -> "Chờ lấy hàng"
            2 -> "Chờ giao hàng"
            3 -> "Đã giao"
            4 -> "Đã huỷ"
            else -> "Trạng thái không hợp lệ"
        }
    }

    fun generateOrderStatusEmailBody(
        orderId: String,
        newStatus: Int,
        finishedAt: String,
        products: List<ProductInfoModel>
    ): String {
        val productHtml = products.joinToString("\n") { product ->
            """
        <div class="product">
            <img src="${product.imageUrl}" alt="${product.name}" width="150" height="150" />
            <p><strong>${product.name}</strong></p>
            <p>Loại: ${product.selectType.type} | ${product.selectType.price}đ</p>
            <p>Số lượng: ${product.quantity}</p>
        </div>
        """
        }

        return """
        <html>
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f4;
                    margin: 0;
                    padding: 0;
                    color: #333;
                }
                .container {
                    width: 100%;
                    max-width: 600px;
                    margin: 20px auto;
                    background-color: #fff;
                    border-radius: 8px;
                    padding: 20px;
                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                }
                .header {
                    text-align: center;
                    padding-bottom: 20px;
                }
                .header h1 {
                    color: #4CAF50;
                    font-size: 24px;
                    margin-bottom: 0;
                }
                .status {
                    background-color: #4CAF50;
                    color: #fff;
                    padding: 10px;
                    border-radius: 5px;
                    text-align: center;
                    margin-bottom: 20px;
                }
                .order-details {
                    margin-bottom: 20px;
                }
                .order-details p {
                    font-size: 16px;
                    margin: 5px 0;
                }
                .product {
                    border-top: 1px solid #eee;
                    padding-top: 10px;
                    margin-top: 10px;
                    text-align: center;
                }
                .footer {
                    text-align: center;
                    font-size: 14px;
                    color: #777;
                    margin-top: 30px;
                }
                .footer p {
                    margin: 5px 0;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Cập nhật trạng thái đơn hàng #$orderId</h1>
                </div>
                <div class="status">
                    <h2>Trạng thái mới: ${convertStatusToString(newStatus)}</h2>
                    <p>Đơn hàng của bạn đã được cập nhật trạng thái thành công.</p>
                </div>
                <div class="order-details">
                    <p><strong>Thời gian hoàn thành:</strong> $finishedAt</p>
                    <p><strong>Danh sách sản phẩm:</strong></p>
                    $productHtml
                </div>
                <div class="footer">
                    <p>Cảm ơn bạn đã mua sắm tại <strong>TechBit</strong> - Cửa hàng linh kiện máy tính điện tử uy tín!</p>
                    <p>Hãy liên hệ với chúng tôi nếu bạn có bất kỳ câu hỏi nào.</p>
                    <p><strong>Đội ngũ TechBit</strong></p>
                    <p>Email: support@techbit.com | Hotline: 1800-1234</p>
                </div>
            </div>
        </body>
        </html>
    """.trimIndent()
    }

    fun generateOrderConfirmationEmailBody(
        orderId: String,
        orderedAt: String,
        products: List<ProductInfoModel>
    ): String {
        val productHtml = products.joinToString("\n") { product ->
            """
        <div class="product">
            <img src="${product.imageUrl}" alt="${product.name}" width="150" height="150" />
            <p><strong>${product.name}</strong></p>
            <p>Loại: ${product.selectType.type}</p>
            <p>Giá: ${"%,d".format(product.selectType.price)}₫</p>
            <p>Số lượng: ${product.quantity}</p>
        </div>
        """
        }

        return """
        <html>
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f4;
                    margin: 0;
                    padding: 0;
                    color: #333;
                }
                .container {
                    width: 100%;
                    max-width: 600px;
                    margin: 20px auto;
                    background-color: #fff;
                    border-radius: 8px;
                    padding: 20px;
                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                }
                .header {
                    text-align: center;
                    padding-bottom: 20px;
                }
                .header h1 {
                    color: #2196F3;
                    font-size: 24px;
                    margin-bottom: 0;
                }
                .order-details {
                    margin-bottom: 20px;
                }
                .order-details p {
                    font-size: 16px;
                    margin: 5px 0;
                }
                .product {
                    border-top: 1px solid #eee;
                    padding-top: 10px;
                    margin-top: 10px;
                    text-align: center;
                }
                .footer {
                    text-align: center;
                    font-size: 14px;
                    color: #777;
                    margin-top: 30px;
                }
                .footer p {
                    margin: 5px 0;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Đơn hàng đã được đặt thành công!</h1>
                </div>
                <div class="order-details">
                    <p><strong>Thời gian đặt hàng:</strong> $orderedAt</p>
                    <p><strong>Danh sách sản phẩm:</strong></p>
                    $productHtml
                </div>
                <div class="footer">
                    <p>Cảm ơn bạn đã mua sắm tại <strong>TechBit</strong> - Cửa hàng linh kiện máy tính điện tử uy tín!</p>
                    <p>Bạn sẽ nhận được email cập nhật khi đơn hàng được xử lý và vận chuyển.</p>
                    <p><strong>Đội ngũ TechBit</strong></p>
                    <p>Email: support@techbit.com | Hotline: 1800-1234</p>
                </div>
            </div>
        </body>
        </html>
    """.trimIndent()
    }


}