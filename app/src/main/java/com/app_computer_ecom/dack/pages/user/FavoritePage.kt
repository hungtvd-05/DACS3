package com.app_computer_ecom.dack.pages.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.components.ProductItem
import com.app_computer_ecom.dack.model.FavoriteModel
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository

@Composable
fun FavoritePage(modifier: Modifier = Modifier) {

    var productList by remember { mutableStateOf(emptyList<ProductModel>()) }
    var favorites by remember { mutableStateOf(emptyList<FavoriteModel>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            favorites = GlobalRepository.favoriteRepository.getFavoritesByUid()
            productList = favorites.mapNotNull {
                GlobalRepository.productRepository.getProductById(it.pid)
            }
        } catch (e: Exception) {
            productList = emptyList()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Danh sách yêu thích",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))


        if (isLoading) {
            LoadingScreen()
        } else {

            if (productList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Không có sản phẩm yêu thích nào",
                        fontSize = 16.sp
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item(span = { GridItemSpan(2) }) {

                }
                items(productList.size) {
                    ProductItem(product = productList[it])
                }
                item(span = { GridItemSpan(2) }) {

                }
            }
        }


    }
}