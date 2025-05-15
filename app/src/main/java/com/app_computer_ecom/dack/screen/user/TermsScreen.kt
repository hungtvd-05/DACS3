package com.app_computer_ecom.dack.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.components.TopBar

@Composable
fun TermsScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopBar(title = "Điều khoản ứng dụng", isShowCard = false) {
                GlobalNavigation.navController.navigate("home/3")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }

                terms.forEachIndexed { index, term ->
                    item {
                        TermsSection(
                            title = "${index + 1}. ${term.title}",
                            content = term.content
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
private fun TermsSection(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 16.sp,
                textAlign = TextAlign.Justify // Căn đều văn bản cho dễ đọc
            )
        }
    }
}

private val terms = listOf(
    Term(
        "Chấp nhận điều khoản",
        "Bằng cách sử dụng ứng dụng DACK Electronics, bạn đồng ý tuân thủ các điều khoản này. Chúng tôi có quyền cập nhật điều khoản mà không cần thông báo trước."
    ),
    Term(
        "Sử dụng dịch vụ",
        "Ứng dụng cung cấp nền tảng để mua linh kiện điện tử. Bạn phải cung cấp thông tin chính xác khi đặt hàng. Cấm sử dụng ứng dụng cho mục đích bất hợp pháp."
    ),
    Term(
        "Thanh toán",
        "Hỗ trợ COD, chuyển khoản, ví điện tử. Giá đã bao gồm VAT, chưa gồm phí vận chuyển. Thanh toán hoàn tất trước giao hàng (trừ COD)."
    ),
    Term(
        "Giao hàng",
        "Giao hàng 2-7 ngày tùy khu vực. Phí vận chuyển dựa trên địa chỉ nhận hàng. Bạn chịu trách nhiệm với địa chỉ đã cung cấp."
    ),
    Term(
        "Đổi trả & hoàn tiền",
        "Đổi trả trong 7 ngày nếu sản phẩm lỗi. Sản phẩm còn nguyên vẹn, kèm hóa đơn. Hoàn tiền trong 14 ngày nếu không đúng mô tả."
    ),
    Term(
        "Bảo mật thông tin",
        "Chúng tôi thu thập thông tin để xử lý đơn hàng. Thông tin được bảo vệ và không chia sẻ với bên thứ ba, trừ đối tác giao hàng."
    ),
    Term(
        "Trách nhiệm pháp lý",
        "Không chịu trách nhiệm thiệt hại gián tiếp từ việc sử dụng ứng dụng. Tranh chấp giải quyết theo luật pháp Việt Nam."
    ),
    Term(
        "Điều khoản bảo trì",
        "Ứng dụng có thể được bảo trì định kỳ. Chúng tôi sẽ thông báo trước nếu ảnh hưởng đến trải nghiệm người dùng."
    ),
    Term(
        "Tài khoản người dùng",
        "Người dùng chịu trách nhiệm với thông tin đăng nhập. Không chia sẻ tài khoản với người khác."
    ),
    Term(
        "Quyền sở hữu trí tuệ",
        "Tất cả nội dung, hình ảnh, mã nguồn thuộc quyền sở hữu của DACK Electronics. Nghiêm cấm sao chép hoặc sử dụng khi chưa được cho phép."
    ),
    Term(
        "Hạn chế trách nhiệm",
        "Chúng tôi không chịu trách nhiệm với các vấn đề ngoài tầm kiểm soát như thiên tai, chiến tranh, gián đoạn mạng."
    ),
    Term(
        "Liên hệ",
        "Email: support@dackelectronics.com\nHotline: 1800-1234 (8:00 - 17:00, T2 - T6)"
    ),
)

data class Term(val title: String, val content: String)
