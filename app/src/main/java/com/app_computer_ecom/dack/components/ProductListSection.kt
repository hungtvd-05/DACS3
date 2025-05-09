package com.app_computer_ecom.dack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository


@Composable
fun ProductListSection(
    categoryId: String? = null,
    brandId: String? = null,
    title: String = ""
) {

    var products by remember {
        mutableStateOf(emptyList<ProductModel>())
    }

    LaunchedEffect(Unit) {
        products = GlobalRepository.productRepository.getProductsWithFilter(
            searchQuery = "",
            brandIds = if (!brandId.isNullOrEmpty()) listOf(brandId) else emptyList(),
            categoryIds = if (!categoryId.isNullOrEmpty()) listOf(categoryId) else emptyList(),
            limit = 8
        )
    }

    if (products.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxHeight(),
                    fontWeight = FontWeight.Medium
                )
                val route =
                    "listproduct/categoryId=${categoryId ?: ""}&brandId=${brandId ?: ""}&searchQuery="
                TextButton(onClick = {
                    GlobalNavigation.navController.navigate(route)
                }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = "Xem thêm",
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "",
                            modifier = Modifier.size(12.dp)
                        )
                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                products.chunked(2).forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        it.forEach { product ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(4.dp)
                            ) {
                                ProductItem(product)
                            }
                        }
                        if (it.size == 1) {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }

        }
    }
}