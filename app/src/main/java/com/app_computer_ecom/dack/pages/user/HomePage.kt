package com.app_computer_ecom.dack.pages.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.app_computer_ecom.dack.viewmodel.AuthViewModel
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun HomePage(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
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

    LaunchedEffect(Unit) {
        products = GlobalRepository.productRepository.getProductsByCreatedAt()
        banners = GlobalRepository.bannerRepository.getBannerByEnable()
        categories = GlobalRepository.categoryRepository.getCategorybyIsEnable()
        brands = GlobalRepository.brandRepository.getBrandByIsEnable()
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        HeaderView(authViewModel, modifier)
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            item(span = { GridItemSpan(2) }) {
                Column(
//                    modifier = modifier
                ) {
                    val pagerState = rememberPagerState(0) {
                        banners.size
                    }
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = 24.dp,
                    ) {
                        AsyncImage(
                            model = banners[it].imageUrl,
                            contentDescription = "Banner",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
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
                        fontSize = 18.sp,
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
                        fontSize = 18.sp,
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
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Sản phẩm mới",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            if (isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Loading()
                }
            } else {
                products.forEachIndexed { index, product ->
                    item {
                        ProductItem(product = product)
                    }
                }
            }
            item(span = { GridItemSpan(2) }) {
                Button(
                    onClick = { GlobalNavigation.navController.navigate("listproduct/categoryId=&brandId=") }
                ) {
                    Text(text = "Xem tất cả")
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
            containerColor = MaterialTheme.colorScheme.background
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
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                textAlign = TextAlign.Center,
                maxLines = 1
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
            containerColor = MaterialTheme.colorScheme.background
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
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = brand.name,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}