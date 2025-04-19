package com.app_computer_ecom.dack.screen.admin

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.admin.HeaderViewMenu
import com.app_computer_ecom.dack.model.ImageInfo
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.ProductRepository
import com.app_computer_ecom.dack.repository.impl.ProductRepositoryImpl
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductScreen(navController: NavHostController) {
    val productRepository: ProductRepository = remember { ProductRepositoryImpl() }
    var productList by remember { mutableStateOf(emptyList<ProductModel>()) }

    LaunchedEffect(Unit) {
        productList = productRepository.getProducts()
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
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            itemsIndexed(productList) {index, product ->
                Items(
                    product = product,
                    onClick = {
                        navController.navigate("admin/editproduct/${product.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun Items(product: ProductModel, onClick: () -> Unit) {

    Card (
        onClick = onClick,
        modifier = Modifier
//            .padding(top = topPadding, bottom = bottomPadding)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color(0xFF808080),
                ambientColor = Color(0xFF505050)
            )
            .background(Color(255, 251, 255), shape = RoundedCornerShape(10.dp))
            .wrapContentHeight()

    ) {
//        Row {
//
//        }
        ProductImage(item=product.imageUrls[0])
//        ProductDetails(item=item)
    }
}

@Composable
fun ProductImage(item: ImageInfo) {
    AsyncImage(
        model = item.imageUrl,
        contentDescription = "",
        modifier = Modifier
            .size(130.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.grey), shape = RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun RowScope.ProductDetails(item: ProductModel) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxHeight()
            .weight(1f),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

    }
}