package com.app_computer_ecom.dack.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.components.TopBar
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.model.ProductInfoModel
import com.app_computer_ecom.dack.model.RatingModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CheckRatingAndCommentScreen(orderId: String, selectedProduct: Int) {
    var order by remember { mutableStateOf<OrderModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var productInfoModel by remember { mutableStateOf<ProductInfoModel?>(null) }
    var ratingModel by remember { mutableStateOf<RatingModel?>(null) }
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    LaunchedEffect(Unit) {
        order = GlobalRepository.orderRepository.getOrderById(orderId)
        productInfoModel = order!!.listProduct[selectedProduct]
        ratingModel = GlobalRepository.ratingAndCommentRepository.getRatingAndCommentById(productInfoModel!!.ratingId)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar(title = "Đánh giá sản phẩm") {
            GlobalNavigation.navController.popBackStack()
        }
        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn (
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(8.dp)
                    ) {
                        ProductItemOrder(
                            productInfoModel = productInfoModel!!,
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("Tổng số tiền : ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(formatter.format(productInfoModel!!.quantity * productInfoModel!!.selectType.price))
                                }
                            },
                            fontSize = 12.sp,
                            lineHeight = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(8.dp)
                    ) {
                        Row {

                            IconButton(
                                onClick = {

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (ratingModel!!.rating >= 1) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                            IconButton(
                                onClick = {

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (ratingModel!!.rating >= 2) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                            IconButton(
                                onClick = {

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (ratingModel!!.rating >= 3) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                            IconButton(
                                onClick = {

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (ratingModel!!.rating >= 4) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                            IconButton(
                                onClick = {

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (ratingModel!!.rating == 5) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                        }
                        OutlinedTextField(
                            value = ratingModel!!.commentModel!!.content,
                            onValueChange = {

                            },
                            label = {
                                Text(
                                    text = "Mô tả, đánh giá sản phẩm",
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 14.sp),
                            readOnly = true,
                            minLines = 3
                        )
                    }
                }
                if (ratingModel!!.commentModel!!.reply.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(8.dp)
                        ) {
                            OutlinedTextField(
                                value = ratingModel!!.commentModel!!.reply,
                                onValueChange = {

                                },
                                label = {
                                    Text(
                                        text = "Phản hồi của shop",
                                        fontSize = 12.sp
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                readOnly = true,
                                minLines = 3
                            )
                        }
                    }
                }
            }
        }
    }
}