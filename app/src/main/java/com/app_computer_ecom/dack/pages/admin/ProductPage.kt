package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductPage() {
    var productList by remember { mutableStateOf(emptyList<ProductModel>()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        productList = GlobalRepository.productRepository.getProducts()
        isLoading = false
    }


    Column {
        if (isLoading) {
            LoadingScreen()
        } else {
            if (productList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Không có sản phẩm nào")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(productList) { index, product ->
                        Items(
                            product = product,
                            onClick = {
                                GlobalNavigation.navController.navigate("admin/editproduct/${product.id}")
                            },
                            delete = {
                                scope.launch {
                                    GlobalRepository.productRepository.deleteProduct(product)
                                    productList = productList.filter { it.id != product.id }
                                }
                            },
                            onClickShowHidden = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        GlobalRepository.productRepository.showHiddenProduct(product)
                                        productList =
                                            GlobalRepository.productRepository.getProducts()
                                    } catch (e: Exception) {

                                    }
                                }
                            },
                            isEnable = product.show,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Button(
                    onClick = {
                        GlobalNavigation.navController.navigate("admin/addproduct")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Thêm sản phẩm",
                        color = Color.White,
                    )
                }
            }

        }

    }
}

@Composable
fun Items(
    product: ProductModel,
    onClick: () -> Unit,
    delete: () -> Unit,
    onClickShowHidden: () -> Unit,
    isEnable: Boolean
) {

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    var minPrice by remember { mutableStateOf(0) }
    var maxPrice by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }


    if (product.prices.size == 1) {
        minPrice = product.prices.first().price
        maxPrice = product.prices.first().price
    } else {
        minPrice = product.prices.minOf { it.price }
        maxPrice = product.prices.maxOf { it.price }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .height(146.dp)
            .padding(8.dp)
            .clickable(
                onClick = onClick
            ),
    ) {

        Box() {
            Row {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = product.imageUrls.firstOrNull()?.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                        placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
                    )
                }
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 3,
                        minLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        product.prices.forEach(
                            action = {
                                Row(
                                    modifier = Modifier
                                        .background(Color(233, 233, 233))
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = it.type,
                                        fontSize = 8.sp,
                                        lineHeight = 10.sp,
                                        color = Color.Black
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        )
                    }
                    Text(
                        text = if (minPrice != maxPrice) "${
                            formatter.format(
                                minPrice
                            )
                        } - ${
                            formatter.format(
                                maxPrice
                            )
                        }" else formatter.format(minPrice),
                        fontSize = 12.sp,
                        color = Color.Red,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(0.dp, 8.dp)
            ) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        modifier = Modifier
                            .size(22.dp),
                        tint = Color.Red
                    )
                }
                IconButton(onClick = { onClickShowHidden() }) {
                    Icon(
                        painter = painterResource(id = if (isEnable) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier
                            .size(22.dp),
                        tint = Color(30, 136, 229)
                    )
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Xác nhận") },
                text = { Text("Bạn có chắc chắn muốn xóa sản phẩm này?") },
                confirmButton = {
                    TextButton(onClick = {
                        delete()
                    }) {
                        Text("Xóa")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }


    }
}