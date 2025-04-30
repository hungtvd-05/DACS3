package com.app_computer_ecom.dack.screen.admin

import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
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
import androidx.navigation.NavHostController
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

@Composable
fun UpdateProductScreen(navigaController: NavHostController, productId: String) {
    val productRepository: ProductRepository = remember { ProductRepositoryImpl() }
    val categoryRepository: CategoryRepository = remember { CategoryRepositoryImpl() }
    val brandRepository: BrandRepository = remember { BrandRepositoryImpl() }

    var product by remember { mutableStateOf<ProductModel?>(null) }

    var brandList by remember { mutableStateOf(emptyList<BrandModel>()) }
    var categoryList by remember { mutableStateOf(emptyList<CategoryModel>()) }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val isUploading = remember { mutableStateOf(false) }
    var productName = remember { mutableStateOf(product?.name ?: "") }
    var productBrand = remember { mutableStateOf("") }
    var productCategory = remember { mutableStateOf("") }
    var productDescription = remember { mutableStateOf("") }
    var productPrices = remember { mutableStateListOf<PriceInfo>() }
    var productImages = remember { mutableStateOf<MutableList<ImageInfo>>(mutableListOf()) }
    var newProductImages = remember { mutableStateOf<MutableList<ImageInfo>>(mutableListOf()) }
    var imageUris by rememberSaveable(stateSaver = UriListSaver()) { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        if (uris != null && uris.isNotEmpty()) {
            imageUris += uris
            newProductImages.value = newProductImages.value.toMutableList().apply {
                addAll(uris.map { ImageInfo() })
            }
        }
    }

    LaunchedEffect(Unit) {
        product = productRepository.getProductById(productId)
        categoryList = categoryRepository.getCategories()
        brandList = brandRepository.getBrands()
        isLoading = false
        if (product != null) {
            productName.value = product!!.name
            productBrand.value = product!!.brandId
            productCategory.value = product!!.categoryId
            productDescription.value = product!!.description
            productPrices.clear() // Xóa các phần tử cũ
            productPrices.addAll(product?.prices ?: emptyList())
            productImages.value.clear()
            productImages.value.addAll(product?.imageUrls ?: emptyList())
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { navigaController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Cập nhật sản phẩm",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = productName.value,
                        onValueChange = { productName.value = it },
                        label = {
                            Text(
                                text = "Tên sản phẩm",
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
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
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
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
                            text = "Nhãn, Giá, Số lượng",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        androidx.compose.material.IconButton(
                            onClick = {
                                productPrices.add(PriceInfo())
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
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = item.type,
                            onValueChange = { newValue ->
                                productPrices[index] = item.copy(type = newValue)
                            },
                            placeholder = { Text(
                                "Nhãn " + (index + 1),
                                fontSize = 12.sp
                            ) },
                            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                            singleLine = true,
                            modifier = Modifier
                                .weight(2f)
                                .padding(end = 4.dp)
                        )

                        OutlinedTextField(
                            value = item.price.toString(),
                            onValueChange = { newValue ->
                                productPrices[index] = item.copy(price = newValue.toIntOrNull() ?: 0)
                            },
                            placeholder = { Text(
                                "Giá",
                                fontSize = 12.sp
                            ) },
                            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        )

                        OutlinedTextField(
                            value = item.quantity.toString(),
                            onValueChange = { newValue ->
                                productPrices[index] = item.copy(quantity = newValue.toIntOrNull() ?: 0)
                            },
                            placeholder = { Text(
                                "Số lượng",
                                fontSize = 12.sp
                            ) },
                            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
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
                                tint = Color.Red
                            )
                        }
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
                            text = "Ảnh",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                launcher.launch("image/*")
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add"
                            )
                        }
                    }
                }

                item {
                    LazyRow {
                        itemsIndexed(productImages.value) { index, imageInfo ->
                            ImagePreviewItem(
                                uri = imageInfo.imageUrl,
                                onClick = {
                                    productImages.value = productImages.value.toMutableList().apply {
                                        remove(imageInfo)
                                    }
                                },
                                onClickShowHidden = {
                                    productImages.value = productImages.value.toMutableList().apply {
                                        this[index] = imageInfo.copy(isHidden = !imageInfo.isHidden)
                                    }
                                },
                                isEnable = !imageInfo.isHidden,
                                modifier = Modifier.height(150.dp)
                            )
                        }
                        itemsIndexed(newProductImages.value) { index, imageInfo ->
                            ImagePreviewItem(
                                uri = imageUris[index].toString(),
                                onClick = {
                                    imageUris -= imageUris[index]
                                    newProductImages.value = newProductImages.value.toMutableList().apply {
                                        remove(imageInfo)
                                    }
                                },
                                onClickShowHidden = {
                                    newProductImages.value = newProductImages.value.toMutableList().apply {
                                        this[index] = imageInfo.copy(isHidden = !imageInfo.isHidden)
                                    }
                                },
                                isEnable = !imageInfo.isHidden,
                                modifier = Modifier.height(150.dp)
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            when {
                                TextUtils.isEmpty(productName.value.toString()) -> {
                                    Toast.makeText(context, "Bạn chưa nhập tên sản phẩm", Toast.LENGTH_SHORT).show()
                                }
                                productImages.value.isEmpty() and newProductImages.value.isEmpty() -> {
                                    Toast.makeText(context, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show()
                                }
                                productPrices.any { it.type.isEmpty() } -> {
                                    Toast.makeText(context, "Có giá sản phẩm chưa nhập loại", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    isUploading.value = true
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            newProductImages.value.forEachIndexed { index, imageInfo ->
                                                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUris[index])
                                                val result = ImageCloudinary.uploadImage(context, bitmap)
                                                result.onSuccess { url ->
                                                    newProductImages.value[index] = imageInfo.copy(imageUrl = url)
                                                }
                                            }

                                            productImages.value.addAll(newProductImages.value)

//                                            var product = ProductModel(
//                                                name = productName.value,
//                                                categoryId = productCategory.value,
//                                                brandId = productBrand.value,
//                                                description = productDescription.value,
//                                                prices = productPrices.toMutableList(),
//                                                imageUrls = productImages.value
//                                            )
                                            
//                                            var newProduct = product
                                            product?.name = productName.value
                                            product?.categoryId = productCategory.value
                                            product?.brandId = productBrand.value
                                            product?.description = productDescription.value
                                            product?.prices = productPrices
                                            product?.imageUrls = productImages.value

                                            product?.let { productRepository.updateProduct(it) }

                                            withContext(Dispatchers.Main) {
                                                navigaController.navigate("admin/product")
                                                isUploading.value = false
                                            }

                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                isUploading.value = false
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cập nhật sản phẩm"
                        )
                    }
                }
            }
        }
    }

}