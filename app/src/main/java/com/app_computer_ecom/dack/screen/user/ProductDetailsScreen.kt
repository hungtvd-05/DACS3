package com.app_computer_ecom.dack.screen.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.components.ProductItem
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.PriceInfo
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailsScreen(productId: String) {
    var product by remember { mutableStateOf<ProductModel?>(null) }
    var category by remember { mutableStateOf<CategoryModel?>(null) }
    var brand by remember { mutableStateOf<BrandModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedPriceInfo by remember { mutableStateOf<PriceInfo?>(null) }

    var ortherProducts by remember {
        mutableStateOf(emptyList<ProductModel>())
    }

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    var minPrice by remember { mutableStateOf(0) }
    var maxPrice by remember { mutableStateOf(0) }



    LaunchedEffect(Unit) {
        product = GlobalRepository.productRepository.getProductById(productId)
        category = GlobalRepository.categoryRepository.getCategorybyId(product!!.categoryId)
        brand = GlobalRepository.brandRepository.getBrandById(product!!.brandId)
        ortherProducts = GlobalRepository.productRepository.getProductsByCategoryIdAndBrandId(
            categoryIds = setOf(product!!.categoryId).toList(), limit = 8
        )
        minPrice = product!!.prices.minOf { it.price as Int }
        maxPrice = product!!.prices.maxOf { it.price as Int }
        isLoading = false
    }

    if (isLoading) {

    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    GlobalNavigation.navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "ShoppingCart"
                        )
                    }

                    IconButton(
                        onClick = {
                            GlobalNavigation.navController.navigate("home")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    }
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = product?.name.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Column(
                    ) {
                        val pagerState = rememberPagerState(0) {
                            product!!.imageUrls.size
                        }
                        HorizontalPager(
                            state = pagerState,
                            pageSpacing = 24.dp,
                        ) {
                            AsyncImage(
                                model = product!!.imageUrls.get(it).imageUrl,
                                contentDescription = "Product images",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        DotsIndicator(
                            dotCount = product!!.imageUrls.size,
                            type = ShiftIndicatorType(
                                DotGraphic(
                                    color = MaterialTheme.colorScheme.primary,
                                    size = 6.dp
                                )
                            ),
                            pagerState = pagerState
                        )
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedPriceInfo == null && minPrice != maxPrice) "${
                                formatter.format(
                                    minPrice
                                )
                            } - ${
                                formatter.format(
                                    maxPrice
                                )
                            }" else if (selectedPriceInfo == null) formatter.format(minPrice) else formatter.format(
                                selectedPriceInfo!!.price
                            ),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        product!!.prices.forEach { priceInfo ->
                            OutlinedButton(
                                onClick = {
                                    selectedPriceInfo = priceInfo
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedPriceInfo == priceInfo) Color(
                                        84,
                                        110,
                                        122
                                    ) else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = priceInfo.type,
                                    color = if (selectedPriceInfo == priceInfo) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Button(
                        onClick = {}, modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Thêm vào giỏ hàng", fontSize = 16.sp)
                    }
                }
                if (category != null) {
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item(span = { GridItemSpan(2) }) {
                        Row {
                            Text(
                                text = "Danh mục : ",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                            )
                            Text(
                                text = category!!.name,
                                fontSize = 18.sp,
                            )
                        }
                    }
                }
                if (brand != null) {
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item(span = { GridItemSpan(2) }) {
                        Row {
                            Text(
                                text = "Thương hiệu : ",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                            )
                            Text(
                                text = brand!!.name,
                                fontSize = 18.sp,
                            )
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Mô tả sản phẩm : ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = product!!.description,
                        fontSize = 16.sp
                    )
                }
                if (ortherProducts.isNotEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = "Sản phẩm liên quan : ",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                        )
                    }
                    ortherProducts.forEach { product ->
                        if (product.id != productId) {
                            item {
                                ProductItem(product = product)
                            }
                        }
                    }
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}