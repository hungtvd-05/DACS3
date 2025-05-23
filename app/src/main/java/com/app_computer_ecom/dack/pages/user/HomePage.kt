package com.app_computer_ecom.dack.pages.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.HeaderView
import com.app_computer_ecom.dack.components.ProductItem
import com.app_computer_ecom.dack.model.BannerModel
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }

    var products by remember {
        mutableStateOf(emptyList<ProductModel>())
    }

    var banners by remember {
        mutableStateOf(emptyList<BannerModel>())
    }

    var categories by remember {
        mutableStateOf(emptyList<CategoryModel>())
    }

    var brands by remember { mutableStateOf(emptyList<BrandModel>()) }

    var productListByBrand by remember { mutableStateOf(mapOf<BrandModel, List<ProductModel>>()) }

    LaunchedEffect(Unit) {
        products = GlobalRepository.productRepository.getProductsByCreatedAt()
        banners = GlobalRepository.bannerRepository.getBannerByEnable()
        categories = GlobalRepository.categoryRepository.getCategorybyIsEnable()
        brands = GlobalRepository.brandRepository.getBrandByIsEnable()
        productListByBrand = brands.associateWith { brand ->
            GlobalRepository.productRepository.getProductsWithFilter(
                searchQuery = "",
                brandIds = listOf(brand.id),
                limit = 8
            )
        }
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        HeaderView(modifier)
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            item(span = { GridItemSpan(2) }) {
                Column {
                    val pagerState = rememberPagerState(0) {
                        banners.size
                    }
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            pageSpacing = 24.dp,
                        ) {

                            AsyncImage(
                                model = banners[it].imageUrl,
                                contentDescription = "Banner",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    DotsIndicator(
                        dotCount = banners.size, type = ShiftIndicatorType(
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
                    text = "Danh mục",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            item(span = { GridItemSpan(2) }) {
                if (isLoading) {
                    Loading()
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        items(categories.size) {
                            CategoryItem(category = categories[it])
                        }
                    }
                }
            }
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Thương hiệu",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            item(span = { GridItemSpan(2) }) {
                if (isLoading) {
                    Loading()
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(brands.size) {
                            BrandItem(brand = brands[it])
                        }
                    }
                }
            }

            productListByBrand.forEach { (brand, productsByBrand) ->
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = brand.name,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxHeight(),
                            fontWeight = FontWeight.Medium
                        )
                        TextButton(onClick = {
                            GlobalNavigation.navController.navigate("listproduct/categoryId=&brandId=${brand.id}&searchQuery=")
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
                }
                productsByBrand.forEachIndexed { index, product ->
                    item {
                        ProductItem(product = product, lastIndexPage = 0)
                    }
                }
            }


            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Sản phẩm mới",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            if (isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Loading()
                }
            } else {
                products.forEachIndexed { index, product ->
                    item {
                        ProductItem(product = product, lastIndexPage = 0)
                    }
                }
            }
            item(span = { GridItemSpan(2) }) {
                Button(
                    onClick = { GlobalNavigation.navController.navigate("listproduct/categoryId=&brandId=&searchQuery=") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(text = "Xem tất cả", color = Color.White)
                }
            }


        }
    }
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CategoryItem(category: CategoryModel) {
    Card(
        onClick = {
            GlobalNavigation.navController.navigate("listproduct/categoryId=${category.id}&brandId=&searchQuery=")
        },
        modifier = Modifier
            .padding(5.dp)
            .size(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                textAlign = TextAlign.Center,
                maxLines = 1,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun BrandItem(brand: BrandModel) {
    Card(
        onClick = { GlobalNavigation.navController.navigate("listproduct/categoryId=&brandId=${brand.id}&searchQuery=") },
        modifier = Modifier
            .padding(5.dp)
            .size(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = brand.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = brand.name,
                textAlign = TextAlign.Center,
                maxLines = 1,
                fontSize = 12.sp
            )
        }
    }
}