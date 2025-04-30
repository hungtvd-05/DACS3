package com.app_computer_ecom.dack.screen.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.admin.HeaderViewMenu
import com.app_computer_ecom.dack.model.ImageInfo
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.repository.ProductRepository
import com.app_computer_ecom.dack.repository.impl.ProductRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductScreen(navController: NavHostController) {
//    val productRepository: ProductRepository = remember { ProductRepositoryImpl() }
    var productList by remember { mutableStateOf(emptyList<ProductModel>()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        productList = GlobalRepository.productRepository.getProducts()
        isLoading = false
    }

    BackHandler(enabled = true) {
        navController.navigate("admin/01") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
    Column {
        HeaderViewMenu(
            title = "Danh sách sản phẩm",
            onBackClick = { navController.navigate("admin/01") },
            onAddClick = {
                navController.navigate("admin/addproduct")
            },
            isUploading = false
        )
        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                item {
                    Spacer(modifier = Modifier.height(2.dp))
                }
                itemsIndexed(productList) {index, product ->
                    Items(
                        product = product,
                        onClick = {
                            navController.navigate("admin/editproduct/${product.id}")
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
                                    productList = GlobalRepository.productRepository.getProducts()
                                } catch (e: Exception) {

                                }
                            }
                        },
                        isEnable = product.show,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }

    }
}

@Composable
fun Items(product: ProductModel, onClick: () -> Unit, delete: () -> Unit, onClickShowHidden: () -> Unit, isEnable: Boolean) {

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    var minPrice by remember { mutableStateOf(0) }
    var maxPrice by remember { mutableStateOf(0) }

    if (product.prices.size == 1) {
        minPrice = product.prices.first().price as Int
        maxPrice = product.prices.first().price as Int
    } else {
        minPrice = product.prices.minOf { it.price as Int }
        maxPrice = product.prices.maxOf { it.price as Int }
    }



    Card (
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(146.dp)
            .padding(bottom = 8.dp)

    ) {
        Row {
            AsyncImage(
                model = product.imageUrls.firstOrNull()?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
            )
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
                                Text(text = it.type, fontSize = 8.sp, lineHeight = 10.sp)
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
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = { delete() }) {
                        Icon(imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = { onClickShowHidden() }) {
                        Icon(painter = painterResource(id = if (isEnable) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color(30, 136, 229)
                        )
                    }
                }
            }
        }
    }
}