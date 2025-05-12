package com.app_computer_ecom.dack.screen.user

import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.components.TopBar
import com.app_computer_ecom.dack.model.CommentModel
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.model.ProductInfoModel
import com.app_computer_ecom.dack.model.RatingModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel
import com.app_computer_ecom.dack.viewmodel.ImageCloudinary
import com.app_computer_ecom.dack.viewmodel.UriListSaver
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RatingAndCommentScreen(orderId: String, selectedProduct: Int) {

    var order by remember { mutableStateOf<OrderModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var productInfoModel by remember { mutableStateOf<ProductInfoModel?>(null) }
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    var imageUris by rememberSaveable(stateSaver = UriListSaver()) {
        mutableStateOf<List<Uri>>(
            emptyList()
        )
    }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        order = GlobalRepository.orderRepository.getOrderById(orderId)
        productInfoModel = order!!.listProduct[selectedProduct]
        isLoading = false
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        if (uris != null && uris.isNotEmpty()) {
            imageUris += uris
        }
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
                                    rating = 1
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (rating >= 1) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                            IconButton(
                                onClick = {
                                    rating = 2
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (rating >= 2) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                            IconButton(
                                onClick = {
                                    rating = 3
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (rating >= 3) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                            IconButton(
                                onClick = {
                                    rating = 4
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (rating >= 4) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                            IconButton(
                                onClick = {
                                    rating = 5
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (rating == 5) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Hình ảnh kèm theo",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            IconButton(
                                onClick = {
                                    launcher.launch("image/*")
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            itemsIndexed(imageUris) { index, uri ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize().padding(bottom = 8.dp),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = "",
                                        modifier = Modifier.height(130.dp),
                                        contentScale = ContentScale.Crop
                                    )

                                    IconButton(onClick = {
                                        imageUris = imageUris.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    }) {
                                        Icon(imageVector = Icons.Default.Delete,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(30.dp),
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                        OutlinedTextField(
                            value = comment,
                            onValueChange = {
                                comment = it
                            },
                            label = {
                                Text(
                                    text = "Mô tả, đánh giá sản phẩm",
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 14.sp),
                            minLines = 3
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
                        Button(
                            onClick = {
                                if (rating == 0) {
                                    AppUtil.showToast(context, "Bạn chưa đánh giá sản phẩm !!!")
                                } else {
                                    isLoading = true
                                    var imageUrls: MutableList<String> = mutableListOf()
                                    scope.launch {
                                        try {
                                            imageUris.forEachIndexed { index, imageInfo ->
                                                val bitmap = MediaStore.Images.Media.getBitmap(
                                                    context.contentResolver,
                                                    imageUris[index]
                                                )
                                                val result =
                                                    ImageCloudinary.uploadImage(context, bitmap)
                                                result.onSuccess { url ->
                                                    imageUrls.add(url)
                                                }
                                            }
                                            var ratingModel = RatingModel.create(
                                                pid = productInfoModel!!.id,
                                                pname = productInfoModel!!.name,
                                                pimageUrl = productInfoModel!!.imageUrl,
                                                selectType = productInfoModel!!.selectType,
                                                quantity = productInfoModel!!.quantity,
                                                uid = order!!.uid,
                                                uname = GLobalAuthViewModel.getAuthViewModel().userModel!!.name,
                                                avatar = GLobalAuthViewModel.getAuthViewModel().userModel!!.avatar,
                                                oid = order!!.id,
                                                rating = rating,
                                                commentModel = CommentModel.create(
                                                    content = comment.trim(),
                                                    imageUrls = imageUrls
                                                )
                                            )
                                            var ratingId = GlobalRepository.ratingAndCommentRepository.addRatingAndComment(ratingModel)

                                            var newProductInfoModel = productInfoModel!!.copy(
                                                ratingId = ratingId,
                                                ratingStar = rating
                                            )
                                            var newListProduct = order!!.listProduct.toMutableList().apply {
                                                set(selectedProduct, newProductInfoModel)
                                            }
                                            GlobalRepository.orderRepository.updateOrderListProduct(order!!.id, newListProduct)
                                            val product = GlobalRepository.productRepository.getProductById(ratingModel.pid)
                                            if (product != null) {
                                                var numOfReviews = product.numOfReviews + 1
                                                var numOfStars = product.numOfStars + ratingModel.rating
                                                var rating = numOfStars.toDouble() / numOfReviews
                                                GlobalRepository.productRepository.updateProduct(product.copy(
                                                    numOfReviews = numOfReviews,
                                                    numOfStars = numOfStars,
                                                    rating = rating
                                                ))
                                            }
                                        } catch (e: Exception) {

                                        } finally {
//                                            isLoading = false
                                            GlobalNavigation.navController.popBackStack()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Đánh giá",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

