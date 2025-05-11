package com.app_computer_ecom.dack.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.TopBar
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.model.RatingModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ReplyRatingAndCommentScreen(ratingId: String) {
    var isLoading by remember { mutableStateOf(true) }
    var product by remember { mutableStateOf<ProductModel?>(null) }
    var ratingModel by remember { mutableStateOf<RatingModel?>(null) }
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    LaunchedEffect(Unit) {
        ratingModel = GlobalRepository.ratingAndCommentRepository.getRatingAndCommentById(ratingId)
        product = GlobalRepository.productRepository.getProductById(ratingModel!!.pid)
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
                        ProductItemRating(
                            ratingModel = ratingModel!!,
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("Tổng số tiền : ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(formatter.format(ratingModel!!.quantity * ratingModel!!.selectType.price))
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

@Composable
fun ProductItemRating(
    ratingModel: RatingModel
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Column {
        Row(
            modifier = Modifier
                .height(100.dp)
                .padding(bottom = 8.dp)
                .clickable(
                    onClick = {
                        GlobalNavigation.navController.navigate("product-details/productId=${ratingModel.pid}")
                    }
                )
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = ratingModel.pimageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                    placeholder = painterResource(id = R.drawable.loading_svgrepo_com),
                    modifier = Modifier.background(Color.White)
                )
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = ratingModel.pname,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 3,
                    minLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .background(Color(233, 233, 233))
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = ratingModel.selectType.type,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp, lineHeight = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row {
                    Text(
                        text = formatter.format(ratingModel.selectType.price),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "x${ratingModel.quantity}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}