package com.app_computer_ecom.dack.screen.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.ProductItem
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.pages.user.Loading
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListProductScreen(categoryId: String = "", brandId: String = "", searchQuery: String = "") {
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

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        selectedCategoryIds = if (categoryId.isNotEmpty()) setOf(categoryId) else emptySet()
        selectedBrandIds = if (brandId.isNotEmpty()) setOf(brandId) else emptySet()
        products = GlobalRepository.productRepository.getProductsWithFilter(
            searchQuery = searchQuery,
            categoryIds = selectedCategoryIds.toList(),
            brandIds = selectedBrandIds.toList(),
            minPrice = minPrice.toDouble(),
            maxPrice = if (maxPrice == 0) Double.MAX_VALUE else maxPrice.toDouble()
        )
        categories = GlobalRepository.categoryRepository.getCategorybyIsEnable()
        brands = GlobalRepository.brandRepository.getBrandByIsEnable()
        isLoading = false

    }

    LaunchedEffect(selectedCategoryIds, selectedBrandIds, minPrice, maxPrice, searchQuery) {
        isLoading = true
        products = GlobalRepository.productRepository.getProductsWithFilter(
            searchQuery = searchQuery,
            categoryIds = selectedCategoryIds.toList(),
            brandIds = selectedBrandIds.toList(),
            minPrice = minPrice.toDouble(),
            maxPrice = if (maxPrice == 0) Double.MAX_VALUE else maxPrice.toDouble()
        )
        isLoading = false
    }

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
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
                        contentDescription = "Back"
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (searchQuery.isNotEmpty()) searchQuery else "Search",
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 20.sp
                    )
                }

                IconButton(
                    onClick = {
                        tempSelectedCategoryIds = selectedCategoryIds
                        tempSelectedBrandIds = selectedBrandIds
                        tempMaxPrice = maxPrice
                        tempMinPrice = minPrice
                        scope.launch { showSheet = true }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter_svgrepo_com),
                        contentDescription = "Add",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }

        if (isLoading) {
            Loading()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(1.dp))
                }
                items(products.size) {
                    ProductItem(product = products[it])
                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(10.dp))
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

