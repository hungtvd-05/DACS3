package com.app_computer_ecom.dack.screen.admin

import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.components.ImagePreviewItem
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.ImageInfo
import com.app_computer_ecom.dack.model.PriceInfo
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.BrandRepository
import com.app_computer_ecom.dack.repository.CategoryRepository
import com.app_computer_ecom.dack.repository.ProductRepository
import com.app_computer_ecom.dack.repository.impl.BrandRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.CategoryRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.ProductRepositoryImpl
import com.app_computer_ecom.dack.viewmodel.ImageCloudinary
import com.app_computer_ecom.dack.viewmodel.UriListSaver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun AddProductScreen() {

    BackHandler(enabled = true) {
        GlobalNavigation.navController.navigate("admin/1") {
            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    val categoryRepository: CategoryRepository = remember { CategoryRepositoryImpl() }
    val brandRepository: BrandRepository = remember { BrandRepositoryImpl() }
    val productRepository: ProductRepository = remember { ProductRepositoryImpl() }

    var brandList by remember { mutableStateOf(emptyList<BrandModel>()) }
    var categoryList by remember { mutableStateOf(emptyList<CategoryModel>()) }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        categoryList = categoryRepository.getCategories()
        brandList = brandRepository.getBrands()
        isLoading = false
    }

    val context = LocalContext.current
    val isUploading = remember { mutableStateOf(false) }
    var productName = remember { mutableStateOf("") }
    var productBrand = remember { mutableStateOf("") }
    var productCategory = remember { mutableStateOf("") }
    var productDescription = remember { mutableStateOf("") }
    var productPrices = remember { mutableStateListOf<PriceInfo>() }
    var productImages = remember { mutableStateOf<MutableList<ImageInfo>>(mutableListOf()) }
    var imageUris by rememberSaveable(stateSaver = UriListSaver()) {
        mutableStateOf<List<Uri>>(
            emptyList()
        )
    }


    if (productPrices.isEmpty()) {
        productPrices.add(PriceInfo.create(id = UUID.randomUUID().toString().replace("-", "").take(6)))
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        if (uris != null && uris.isNotEmpty()) {
            imageUris += uris
            productImages.value = productImages.value.toMutableList().apply {
                addAll(uris.map { ImageInfo() })
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize().background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { GlobalNavigation.navController.navigate("admin/1") },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "Thêm sản phẩm",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (isLoading) {
                LoadingScreen()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = productName.value,
                            onValueChange = { productName.value = it },
                            label = {
                                Text(
                                    text = "Tên sản phẩm",
                                    fontSize = 10.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 12.sp),
                            singleLine = true
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        OutlinedTextField(
                            value = productDescription.value,
                            onValueChange = { productDescription.value = it },
                            label = {
                                Text(
                                    text = "Mô tả",
                                    fontSize = 10.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 12.sp),
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Danh mục",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            CategoryDropDownFun(
                                categoryList = categoryList,
                                selectedCategoryId = productCategory.value,
                                onCategorySelected = { productCategory.value = it }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Thương hiệu",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            BrandDropDownFun(
                                brandList = brandList,
                                selectedBrandId = productBrand.value,
                                onBrandSelected = { productBrand.value = it }
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nhãn, Số lượng, Giá",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    productPrices.add(PriceInfo.create(id = UUID.randomUUID().toString().replace("-", "").take(6)))
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add"
                                )
                            }
                        }
                    }


                    itemsIndexed(productPrices) { index, item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                OutlinedTextField(
                                    value = item.type,
                                    onValueChange = { newValue ->
                                        productPrices[index] = item.copy(type = newValue)
                                    },
                                    placeholder = {
                                        Text(
                                            "Nhãn " + (index + 1),
                                            fontSize = 10.sp
                                        )
                                    },
                                    textStyle = TextStyle(fontSize = 12.sp),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(2f)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                OutlinedTextField(
                                    value = item.quantity.toString(),
                                    onValueChange = { newValue ->
                                        productPrices[index] =
                                            item.copy(quantity = newValue.toIntOrNull() ?: 0)
                                    },
                                    placeholder = {
                                        Text(
                                            "Số lượng",
                                            fontSize = 10.sp
                                        )
                                    },
                                    textStyle = TextStyle(fontSize = 12.sp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    modifier = Modifier
                                        .weight(1f)

                                )

                                IconButton(
                                    onClick = {
                                        if (productPrices.size > 1) productPrices.remove(item)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "delete",
                                        tint = Color(230, 81, 0),
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = item.price.toString(),
                                    onValueChange = { newValue ->
                                        productPrices[index] =
                                            item.copy(price = newValue.toIntOrNull() ?: 0)
                                    },
                                    placeholder = {
                                        Text(
                                            "Giá",
                                            fontSize = 10.sp
                                        )
                                    },
                                    textStyle = TextStyle(fontSize = 12.sp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    modifier = Modifier
                                        .weight(1f)

                                )
                                Spacer(modifier = Modifier.width(48.dp))
                            }
                        }


                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ảnh",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    launcher.launch("image/*")
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }

                    item {
                        LazyRow {
                            itemsIndexed(productImages.value) { index, imageInfo ->
                                ImagePreviewItem(
                                    uri = imageUris[index].toString(),
                                    onClick = {
                                        imageUris -= imageUris[index]
                                        productImages.value =
                                            productImages.value.toMutableList().apply {
                                                remove(imageInfo)
                                            }
                                    },
                                    onClickShowHidden = {
                                        if (productImages.value.size > 1) {
                                            productImages.value =
                                                productImages.value.toMutableList().apply {
                                                    this[index] =
                                                        imageInfo.copy(isHidden = !imageInfo.isHidden)
                                                }
                                        }
                                    },
                                    isEnable = !imageInfo.isHidden,
                                    modifier = Modifier.height(150.dp)
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    item {
                        Button(
                            onClick = {
                                when {
                                    TextUtils.isEmpty(productName.value.toString()) -> {
                                        Toast.makeText(
                                            context,
                                            "Bạn chưa nhập tên sản phẩm",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    TextUtils.isEmpty(productBrand.value.toString()) -> {
                                        Toast.makeText(
                                            context,
                                            "Bạn chưa chọn thương hiệu",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    TextUtils.isEmpty(productCategory.value.toString()) -> {
                                        Toast.makeText(
                                            context,
                                            "Bạn chưa chọn danh mục",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    productImages.value.isEmpty() -> {
                                        Toast.makeText(context, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    productPrices.any { it.type.isEmpty() } -> {
                                        Toast.makeText(
                                            context,
                                            "Có giá sản phẩm chưa nhập loại",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    else -> {
                                        isLoading = true
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                productImages.value.forEachIndexed { index, imageInfo ->
                                                    val bitmap = MediaStore.Images.Media.getBitmap(
                                                        context.contentResolver,
                                                        imageUris[index]
                                                    )
                                                    val result =
                                                        ImageCloudinary.uploadImage(context, bitmap)
                                                    result.onSuccess { url ->
                                                        productImages.value[index] =
                                                            imageInfo.copy(imageUrl = url)
                                                    }
                                                }

                                                var product = ProductModel.create(
                                                    name = productName.value,
                                                    categoryId = productCategory.value,
                                                    brandId = productBrand.value,
                                                    description = productDescription.value,
                                                    prices = productPrices.toMutableList(),
                                                    imageUrls = productImages.value
                                                )

                                                productRepository.addProduct(product)

                                                withContext(Dispatchers.Main) {
                                                    GlobalNavigation.navController.navigate("admin/1")
                                                    isLoading = false
                                                }

                                            } catch (e: Exception) {
                                                withContext(Dispatchers.Main) {
                                                    isLoading = false
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "Xác nhận",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                }
            }

        }
    }
}

@Composable
fun CategoryDropDownFun(
    categoryList: List<CategoryModel>,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
) {
    var dropControl by remember { mutableStateOf(false) }
    var selectedCategory =
        categoryList.find { it.id == selectedCategoryId }?.name ?: "Chọn danh mục"
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedCard(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                if (categoryList.isNotEmpty()) {
                    dropControl = true
                }
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp)
                    .padding(5.dp)
            ) {
                Text(text = selectedCategory, fontSize = 12.sp)
                Icon(
                    imageVector = if (dropControl) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown"
                )
            }
            DropdownMenu(
                expanded = dropControl,
                onDismissRequest = { dropControl = false }
            ) {
                if (categoryList.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Không có danh mục", fontSize = 12.sp) },
                        onClick = { /* Không làm gì */ },
                        enabled = false
                    )
                } else {
                    categoryList.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name, fontSize = 12.sp) },
                            onClick = {
                                onCategorySelected(category.id)
                                dropControl = false
                            },
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BrandDropDownFun(
    brandList: List<BrandModel>,
    selectedBrandId: String,
    onBrandSelected: (String) -> Unit,
) {
    var dropControl by remember { mutableStateOf(false) }
    var selectedBrand = brandList.find { it.id == selectedBrandId }?.name ?: "Chọn thương hiệu"
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                if (brandList.isNotEmpty()) {
                    dropControl = true
                }
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp)
                    .padding(5.dp)
            ) {
                Text(text = selectedBrand, fontSize = 12.sp)
                Icon(
                    imageVector = if (dropControl) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown"
                )
            }
            DropdownMenu(
                expanded = dropControl,
                onDismissRequest = { dropControl = false }
            ) {
                if (brandList.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Không có thương hiệu", fontSize = 12.sp) },
                        onClick = { /* Không làm gì */ },
                        enabled = false
                    )
                } else {
                    brandList.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name, fontSize = 12.sp) },
                            onClick = {
                                onBrandSelected(category.id)
                                dropControl = false
                            },
                        )
                    }
                }
            }
        }
    }
}
