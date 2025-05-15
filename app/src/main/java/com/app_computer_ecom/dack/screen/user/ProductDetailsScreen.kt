package com.app_computer_ecom.dack.screen.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.ProductItem
import com.app_computer_ecom.dack.data.room.DatabaseProvider
import com.app_computer_ecom.dack.data.room.entity.ProductHistory
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.PriceInfo
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.model.RatingModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.ui.theme.ThemeManager
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailsScreen(productId: String, lastIndexPage: Int, modifier: Modifier = Modifier) {
    BackHandler(enabled = true) {
        if (lastIndexPage == -1) {
            GlobalNavigation.navController.popBackStack()
        } else {
            GlobalNavigation.navController.navigate("home/${lastIndexPage}") {
                popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

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

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                            fontSize = 16.sp,
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
                                fontSize = 14.sp,
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
                                    tint = if (isFavorite) Color(
                                        230,
                                        81,
                                        0
                                    ) else MaterialTheme.colorScheme.onBackground
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
                                        color = if (selectedPriceInfo == priceInfo) Color.White else MaterialTheme.colorScheme.onBackground,
                                        fontSize = 12.sp
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
                            Text(text = "Thêm vào giỏ hàng", fontSize = 14.sp, color = Color.White)
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
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = category!!.name,
                                    fontSize = 14.sp,
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
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = brand!!.name,
                                    fontSize = 14.sp,
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
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = product!!.description,
                            fontSize = 12.sp,
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
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        ortherProducts.forEach { product ->
                            if (product.id != productId) {
                                item {
                                    ProductItem(product = product, lastIndexPage = -1)
                                }
                            }
                        }
                        item(span = { GridItemSpan(2) }) {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }

                    if (product!!.numOfReviews != 0) {
                        item(span = { GridItemSpan(2) }) {
                            CommentSection(product!!)
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun CommentSection(product: ProductModel) {
    var listRatingModel by remember { mutableStateOf(emptyList<RatingModel>()) }
    LaunchedEffect(Unit) {
        listRatingModel =
            GlobalRepository.ratingAndCommentRepository.getRatingAndComment(product.id, 4)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format("%.1f", product.rating),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = Color(241, 179, 59, 255),
                    contentDescription = "rating",
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(14.dp)
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        ) {
                            append("Đánh giá sản phẩm")
                        }
                        append(" ")
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("(${listRatingModel.size})")
                        }
                    },
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            TextButton(onClick = {
                GlobalNavigation.navController.navigate("review/${product.id}")
            }, contentPadding = PaddingValues(0.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tất cả", fontSize = 10.sp, lineHeight = 12.sp)
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        modifier = Modifier.size(12.dp)
                    )

                }
            }

        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            listRatingModel.forEachIndexed { index, ratingModel ->
                CommentItem(ratingModel)
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun CommentItem(
    ratingModel: RatingModel
) {

    var showReply by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ratingModel.avatar,
                contentDescription = "avatar",
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                ratingModel.uname,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(ratingModel.rating) {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = Color(241, 179, 59, 255),
                    contentDescription = "rating",
                    modifier = Modifier
                        .size(10.dp)
                )
            }
        }
        Text(
            text = "Phân loại : ${ratingModel.selectType.type}",
            color = Color.Gray,
            fontSize = 12.sp
        )

        if (ratingModel.commentModel!!.content.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = ratingModel.commentModel.content,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        if (ratingModel.commentModel.imageUrls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(ratingModel.commentModel.imageUrls) { index, item ->
                    AsyncImage(
                        model = item,
                        contentDescription = "",
                        modifier = Modifier.height(96.dp),
                        error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                        placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                SimpleDateFormat("dd-MM-yyyy HH:mm").format(ratingModel.createdAt.toDate()),
                fontSize = 8.sp,
                lineHeight = 8.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (ratingModel.commentModel.reply != "") {
                TextButton(
                    onClick = { showReply = !showReply },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(IntrinsicSize.Min)
                ) {
                    Row {
                        Text(
                            text = "Phản hồi người bán",
                            fontSize = 8.sp,
                            lineHeight = 8.sp
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "",
                            modifier = Modifier.size(10.dp),
                        )
                    }
                }
            }
        }
        if (showReply) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(
                        if (ThemeManager.isDarkTheme) {
                            Color(0xFF1A1A1A)
                        } else {
                            Color(0xFFF3F0F0)
                        }
                    )
                    .padding(8.dp)
            ) {
                Text(
                    "Phản hồi của người bán",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    ratingModel.commentModel.reply,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}