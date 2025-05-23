package com.app_computer_ecom.dack.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.ProductItem
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListProductScreen(categoryId: String = "", brandId: String = "", searchQuery: String = "", modifier: Modifier = Modifier) {
    var products by remember {
        mutableStateOf(emptyList<ProductModel>())
    }

    var brands by remember { mutableStateOf(emptyList<BrandModel>()) }

    var categories by remember {
        mutableStateOf(emptyList<CategoryModel>())
    }

    var selectedCategoryIds by remember { mutableStateOf(setOf<String>()) }
    var selectedBrandIds by remember { mutableStateOf(setOf<String>()) }

    var tempSelectedCategoryIds by remember { mutableStateOf(setOf<String>()) }
    var tempSelectedBrandIds by remember { mutableStateOf(setOf<String>()) }

    var minPrice by remember { mutableStateOf(0) }
    var maxPrice by remember { mutableStateOf(0) }

    var tempMinPrice by remember { mutableStateOf(0) }
    var tempMaxPrice by remember { mutableStateOf(0) }

    val showOptionsStar = listOf("Tất cả", "5 sao", "4 sao trở lên", "3 sao trở lên", "2 sao trở lên", "1 sao trở lên")
    var (tempSelectedOptionStar, onOptionSelectedStar) = remember { mutableStateOf(showOptionsStar[0]) }
    var selectedOptionStar by remember { mutableStateOf(showOptionsStar[0]) }

    var isLoading by remember { mutableStateOf(true) }

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        selectedCategoryIds = if (categoryId.isNotEmpty()) setOf(categoryId) else emptySet()
        selectedBrandIds = if (brandId.isNotEmpty()) setOf(brandId) else emptySet()
        products = GlobalRepository.productRepository.getProductsWithFilter(
            searchQuery = searchQuery,
            categoryIds = selectedCategoryIds.toList(),
            brandIds = selectedBrandIds.toList(),
            minPrice = minPrice.toInt(),
            maxPrice = if (maxPrice == 0) Int.MAX_VALUE else maxPrice.toInt()
        )
        categories = GlobalRepository.categoryRepository.getCategorybyIsEnable()
        brands = GlobalRepository.brandRepository.getBrandByIsEnable()
        isLoading = false

    }

    LaunchedEffect(selectedCategoryIds, selectedBrandIds, minPrice, maxPrice, searchQuery, selectedOptionStar) {
        isLoading = true
        products = GlobalRepository.productRepository.getProductsWithFilter(
            searchQuery = searchQuery,
            categoryIds = selectedCategoryIds.toList(),
            brandIds = selectedBrandIds.toList(),
            minPrice = minPrice.toInt(),
            maxPrice = if (maxPrice == 0) Int.MAX_VALUE else maxPrice.toInt(),
            rating = if (selectedOptionStar == "Tất cả") 0 else selectedOptionStar.split(" ")[0].toInt(),
        )
        isLoading = false
    }

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
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

//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .wrapContentWidth(Alignment.CenterHorizontally),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = if (searchQuery.isNotEmpty()) searchQuery else "Search",
//                        style = TextStyle(fontWeight = FontWeight.Bold),
//                        fontSize = 20.sp,
//                        color = MaterialTheme.colorScheme.onBackground
//                    )
//                }

                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null, // Tắt hiệu ứng ripple mặc định
                                onClick = {
                                    GlobalNavigation.navController.navigate("search") {
                                        popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            ),
                        value = if (searchQuery.isNotEmpty()) searchQuery else "Search",
                        onValueChange = {}, // Không cho phép thay đổi giá trị
                        readOnly = true,
                        enabled = false, // Vô hiệu hóa tương tác mặc định của TextField
                        interactionSource = interactionSource,
                        placeholder = { Text("Search", fontSize = 14.sp) },
                        shape = RoundedCornerShape(
                            16.dp
                        ),
                    )

                    IconButton(
                        onClick = {
                            tempSelectedCategoryIds = selectedCategoryIds
                            tempSelectedBrandIds = selectedBrandIds
                            tempMaxPrice = maxPrice
                            tempMinPrice = minPrice
                            tempSelectedOptionStar = selectedOptionStar
                            onOptionSelectedStar(selectedOptionStar)
                            scope.launch { showSheet = true }
                        },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_svgrepo_com),
                            contentDescription = "filter",
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            if (isLoading) {
                LoadingScreen()
            } else {

                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không tìm thấy sản phẩm",
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        item(span = { GridItemSpan(2) }) {
                        }
                        items(products.size) {
                            ProductItem(product = products[it], lastIndexPage = -1)
                        }
                        item(span = { GridItemSpan(2) }) {
                        }
                    }
                }
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                    ) {
                        item(span = { GridItemSpan(2) }) {
                            Text(text = "Thương hiệu")
                        }
                        brands.forEach { brand ->
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = tempSelectedBrandIds.contains(brand.id),
                                        onCheckedChange = { isChecked ->
                                            tempSelectedBrandIds = if (isChecked) {
                                                tempSelectedBrandIds + brand.id
                                            } else {
                                                tempSelectedBrandIds - brand.id
                                            }
                                        }
                                    )
                                    Text(brand.name)
                                }
                            }
                        }
                        item(span = { GridItemSpan(2) }) {
                            Text(text = "Danh mục")
                        }
                        categories.forEach { category ->
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = tempSelectedCategoryIds.contains(category.id),
                                        onCheckedChange = { isChecked ->
                                            tempSelectedCategoryIds = if (isChecked) {
                                                tempSelectedCategoryIds + category.id
                                            } else {
                                                tempSelectedCategoryIds - category.id
                                            }
                                        }
                                    )
                                    Text(category.name)
                                }
                            }
                        }
                        item(span = { GridItemSpan(2) }) {
                            Text(text = "Đánh giá sản phẩm")
                        }
                        item(span = { GridItemSpan(2) }) {
                            Column(modifier = Modifier.selectableGroup()) {
                                showOptionsStar.forEach { text ->
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .selectable(
                                                selected = (text == tempSelectedOptionStar),
                                                onClick = { onOptionSelectedStar(text) },
                                                role = Role.RadioButton
                                            )
                                            .padding(horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = (text == tempSelectedOptionStar),
                                            onClick = null
                                        )
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                }
                            }

                        }
                        item(span = { GridItemSpan(2) }) {
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = if (tempMinPrice != 0) tempMinPrice.toString() else "",
                                onValueChange = { newValue ->
                                    if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                        tempMinPrice = newValue.toIntOrNull() ?: 0
                                    }
                                },
                                label = {
                                    Text(
                                        "Giá tối thiểu",
                                        fontSize = 12.sp
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = if (tempMaxPrice != 0) tempMaxPrice.toString() else "",
                                onValueChange = { newValue ->
                                    if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                        tempMaxPrice = newValue.toIntOrNull() ?: 0
                                    }
                                },
                                label = {
                                    Text(
                                        "Giá tối đa",
                                        fontSize = 12.sp
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier
                                    .padding(start = 8.dp)
                            )
                        }
                        item(span = { GridItemSpan(2) }) {
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                        item {
                            Button(
                                onClick = {
                                    if (tempMinPrice != 0 && tempMaxPrice != 0 && tempMinPrice > tempMaxPrice) {
                                        AppUtil.showToast(
                                            context,
                                            "Giá tối thiểu phải nhỏ hơn giá tối đa"
                                        )
                                    } else {
                                        minPrice = tempMinPrice
                                        maxPrice = tempMaxPrice
                                    }
                                    selectedBrandIds = tempSelectedBrandIds
                                    selectedCategoryIds = tempSelectedCategoryIds
                                    selectedOptionStar = tempSelectedOptionStar
                                    scope.launch {
                                        sheetState.hide()
                                    }
                                    showSheet = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 8.dp)
                            ) {
                                Text("Áp dụng")
                            }
                        }
                        item {
                            Button(
                                onClick = {
                                    minPrice = 0
                                    maxPrice = 0
                                    selectedBrandIds = setOf()
                                    selectedCategoryIds = setOf()
                                    selectedOptionStar = showOptionsStar[0]
                                    scope.launch {
                                        sheetState.hide()
                                    }
                                    showSheet = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text("Xóa lọc")
                            }
                        }
                    }
                }
            }
        }
    }
}

