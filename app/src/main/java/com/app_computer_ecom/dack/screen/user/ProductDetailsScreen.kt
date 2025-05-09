package com.app_computer_ecom.dack.screen.user

import DatabaseProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.components.ProductItem
import com.app_computer_ecom.dack.data.entity.ProductHistory
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.PriceInfo
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.launch
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

    val scope = rememberCoroutineScope()

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    var minPrice by remember { mutableStateOf(0) }
    var maxPrice by remember { mutableStateOf(0) }


    var isFavorite by remember { mutableStateOf(false) }


    var cartItemCount by remember { mutableStateOf(0) }


    var context = LocalContext.current
    val productHistoryDao = DatabaseProvider.getDatabase(context).productHistoryDao()



    LaunchedEffect(Unit) {
        product = GlobalRepository.productRepository.getProductById(productId)
        category = GlobalRepository.categoryRepository.getCategorybyId(product!!.categoryId)
        brand = GlobalRepository.brandRepository.getBrandById(product!!.brandId)
        ortherProducts = GlobalRepository.productRepository.getProductsByCategoryIdAndBrandId(
            categoryIds = setOf(product!!.categoryId).toList(), limit = 8
        )
        ortherProducts = ortherProducts.dropWhile { it.id == productId }
        minPrice = product!!.prices.minOf { it.price }
        maxPrice = product!!.prices.maxOf { it.price }

        isFavorite = GlobalRepository.favoriteRepository.isFavorite(productId)
        cartItemCount = GlobalRepository.cartRepository.getCartList().first.size
        isLoading = false


    }

    LaunchedEffect(productId) {
        scope.launch {
            productHistoryDao.insertIfNotExists(
                ProductHistory(
                    productId = productId,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                GlobalNavigation.navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.padding(end = 4.dp)
            ) {
                IconButton(
                    onClick = {
                        GlobalNavigation.navController.navigate("home/2")
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart",
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(16.dp)
                                .background(Color(230, 81, 0), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (cartItemCount < 100) cartItemCount.toString() else "99+",
                                color = Color.White,
                                fontSize = 6.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 8.sp
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        GlobalNavigation.navController.navigate("home/0")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        if (isLoading) {
            LoadingScreen()
        } else {
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
                    Column(
                    ) {
                        val pagerState = rememberPagerState(0) {
                            product!!.imageUrls.size
                        }

//                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalPager(
                            state = pagerState,
                            pageSpacing = 24.dp,
                        ) {
                            AsyncImage(
                                model = product!!.imageUrls.get(it).imageUrl,
                                contentDescription = "Product images",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(260.dp)
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
                    Text(
                        text = product?.name.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                        )
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
                            fontWeight = FontWeight.SemiBold,
                            color = Color(230, 81, 0)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (isFavorite) {
                                        GlobalRepository.favoriteRepository.deleteFavorite(
                                            context = context,
                                            productId = productId
                                        )

                                    } else {
                                        GlobalRepository.favoriteRepository.addToFavorite(
                                            context = context,
                                            productId = productId
                                        )
                                    }

                                    isFavorite = !isFavorite

                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isFavorite) Color(230, 81, 0) else MaterialTheme.colorScheme.onBackground
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
                                    containerColor = if (selectedPriceInfo == priceInfo) MaterialTheme.colorScheme.primary else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = priceInfo.type,
                                    color = if (selectedPriceInfo == priceInfo) Color.White else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(2.dp))
                }
                item(span = { GridItemSpan(2) }) {
                    Button(
                        onClick = {
                            if (selectedPriceInfo != null) {
                                scope.launch {
                                    GlobalRepository.cartRepository.addToCart(
                                        context,
                                        productId,
                                        selectedPriceInfo!!
                                    )
                                    cartItemCount += 1
                                }
                            } else {
                                AppUtil.showToast(context, "Vui lòng chọn mẫu sản phẩm !!!")
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        Text(text = "Thêm vào giỏ hàng", fontSize = 16.sp, color = Color.White)
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
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = category!!.name,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground
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
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = brand!!.name,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = product!!.description,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
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
                            color = MaterialTheme.colorScheme.onBackground
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