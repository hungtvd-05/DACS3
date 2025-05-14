package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPage() {
    var productList by remember { mutableStateOf(emptyList<ProductModel>()) }
    var brands by remember { mutableStateOf(emptyList<BrandModel>()) }

    var categories by remember {
        mutableStateOf(emptyList<CategoryModel>())
    }

    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    var selectedCategoryIds by remember { mutableStateOf(setOf<String>()) }
    var selectedBrandIds by remember { mutableStateOf(setOf<String>()) }

    var tempSelectedCategoryIds by remember { mutableStateOf(setOf<String>()) }
    var tempSelectedBrandIds by remember { mutableStateOf(setOf<String>()) }

    var minPrice by remember { mutableStateOf(0) }
    var maxPrice by remember { mutableStateOf(0) }

    var tempMinPrice by remember { mutableStateOf(0) }
    var tempMaxPrice by remember { mutableStateOf(0) }

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val showOptions = listOf("Tất cả", "Hiện", "Ẩn")
    var (tempSelectedOption, onOptionSelected) = remember { mutableStateOf(showOptions[0]) }
    var selectedOption by remember { mutableStateOf(showOptions[0]) }

    val showOptionsStar = listOf("Tất cả", "5 sao", "4 sao trở lên", "3 sao trở lên", "2 sao trở lên", "1 sao trở lên")
    var (tempSelectedOptionStar, onOptionSelectedStar) = remember { mutableStateOf(showOptionsStar[0]) }
    var selectedOptionStar by remember { mutableStateOf(showOptionsStar[0]) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        selectedCategoryIds = emptySet()
        selectedBrandIds = emptySet()
        productList = GlobalRepository.productRepository.getProducts()
        categories = GlobalRepository.categoryRepository.getCategories()
        brands = GlobalRepository.brandRepository.getBrands()
        isLoading = false
    }

    LaunchedEffect(selectedCategoryIds, selectedBrandIds, minPrice, maxPrice, selectedOption, selectedOptionStar) {
        isLoading = true
        productList = GlobalRepository.productRepository.getProductsByCategoryIdAndBrandId(
            categoryIds = selectedCategoryIds.toList(),
            brandIds = selectedBrandIds.toList(),
            minPrice = minPrice.toInt(),
            maxPrice = if (maxPrice == 0) Int.MAX_VALUE else maxPrice.toInt(),
            rating = if (selectedOptionStar == "Tất cả") 0 else selectedOptionStar.split(" ")[0].toInt(),
            isEnable = if (selectedOption == "Hiện") 1 else if (selectedOption == "Ẩn") 0 else -1
        )
        isLoading = false
    }


    Column {
        if (isLoading) {
            LoadingScreen()
        } else {
            if (productList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Không có sản phẩm nào", fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productList.size, key = { index -> productList[index].id }) { index ->
                        Items(
                            product = productList[index],
                            onClick = {
                                GlobalNavigation.navController.navigate("admin/editproduct/${productList[index].id}")
                            },
                            delete = {
                                scope.launch {
                                    GlobalRepository.productRepository.deleteProduct(productList[index])
                                    productList = productList.filter { it.id != productList[index].id }
                                }
                            },
                            onClickShowHidden = {
                                scope.launch {
                                    GlobalRepository.productRepository.showHiddenProduct(productList[index])
                                    if (selectedOption == "Ẩn" || selectedOption == "Hiện") {
                                        productList = productList.filter { it.id != productList[index].id }
                                    } else {
                                        productList = productList.map {
                                            if (it.id == productList[index].id) {
                                                it.copy(show = !it.show)
                                            } else {
                                                it
                                            }
                                        }
                                    }
                                }
                            },
                            isEnable = productList[index].show,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Row {
                    Button(
                        onClick = {
                            GlobalNavigation.navController.navigate("admin/addproduct")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Thêm sản phẩm",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(
                        onClick = {
                            tempSelectedCategoryIds = selectedCategoryIds
                            tempSelectedBrandIds = selectedBrandIds
                            tempMaxPrice = maxPrice
                            tempMinPrice = minPrice
                            tempSelectedOption = selectedOption
                            onOptionSelected(selectedOption)
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
                        Text(text = "Trạng thái sản phẩm")
                    }
                    item(span = { GridItemSpan(2) }) {
                        Column(modifier = Modifier.selectableGroup()) {
                            showOptions.forEach { text ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .selectable(
                                            selected = (text == tempSelectedOption),
                                            onClick = { onOptionSelected(text) },
                                            role = Role.RadioButton
                                        )
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (text == tempSelectedOption),
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
                                selectedOption = tempSelectedOption
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
                                selectedOption = showOptions[0]
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

@Composable
fun Items(
    product: ProductModel,
    onClick: () -> Unit,
    delete: () -> Unit,
    onClickShowHidden: () -> Unit,
    isEnable: Boolean
) {

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    var minPrice by remember { mutableStateOf(0) }
    var maxPrice by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }


    if (product.prices.size == 1) {
        minPrice = product.prices.first().price
        maxPrice = product.prices.first().price
    } else {
        minPrice = product.prices.minOf { it.price }
        maxPrice = product.prices.maxOf { it.price }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .height(146.dp)
            .padding(8.dp)
            .clickable(
                onClick = onClick
            ),
    ) {

        Box() {
            Row {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = product.imageUrls.firstOrNull()?.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                        placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
                    )
                }
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 3,
                        minLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        product.prices.forEach(
                            action = {
                                Row(
                                    modifier = Modifier
                                        .background(Color(233, 233, 233))
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = it.type,
                                        fontSize = 8.sp,
                                        lineHeight = 10.sp,
                                        color = Color.Black
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        )
                    }
                    Text(
                        text = if (minPrice != maxPrice) "${
                            formatter.format(
                                minPrice
                            )
                        } - ${
                            formatter.format(
                                maxPrice
                            )
                        }" else formatter.format(minPrice),
                        fontSize = 12.sp,
                        color = Color(230, 81, 0),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(0.dp, 8.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        modifier = Modifier
                            .size(22.dp),
                        tint = Color(230, 81, 0)
                    )
                }
                IconButton(onClick = { onClickShowHidden() }) {
                    Icon(
                        painter = painterResource(id = if (isEnable) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier
                            .size(22.dp),
                        tint = Color(30, 136, 229)
                    )
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Xác nhận", fontSize = 14.sp) },
                text = { Text("Bạn có chắc chắn muốn xóa sản phẩm này?", fontSize = 12.sp) },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        delete()
                    }) {
                        Text("Xóa", fontSize = 12.sp)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Hủy", fontSize = 12.sp)
                    }
                }
            )
        }


    }
}