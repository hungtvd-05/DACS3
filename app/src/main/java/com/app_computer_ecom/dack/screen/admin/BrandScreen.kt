package com.app_computer_ecom.dack.screen.admin

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.admin.HeaderViewMenu
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.repository.BrandRepository
import com.app_computer_ecom.dack.repository.impl.BrandRepositoryImpl
import com.app_computer_ecom.dack.viewmodel.ImageCloudinary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BrandScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val brandRepository: BrandRepository = remember { BrandRepositoryImpl() }
    val context = LocalContext.current
    BackHandler(enabled = true) {
        navController.navigate("admin/01") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    var brandList by remember { mutableStateOf(emptyList<BrandModel>()) }

    var brandId = remember { mutableStateOf("") }
    var brandName = remember { mutableStateOf("") }
    var brandIsEnable = remember { mutableStateOf(true) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var isUploading = remember { mutableStateOf(false) }
    var isUpdate = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        imageUrl = null
    }

    LaunchedEffect(Unit) {
        brandList = brandRepository.getBrands()
    }

    imageUri?.let {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderViewMenu(
            title = "Danh sách thương hiệu",
            onBackClick = { navController.navigate("admin/01") },
            onAddClick = {
                imageUri = null
                imageUrl = null
                brandName.value = ""
                brandIsEnable.value = true
                brandId.value = ""
                isUpdate.value = false
            },
            isUploading = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .alpha(if (isUploading.value) 0.5f else 1f)
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { launcher.launch("image/*") }
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("Chọn ảnh")
                        }
                    }
                }
                Column {
                    OutlinedTextField(
                        value = brandName.value,
                        onValueChange = { brandName.value = it },
                        label = {
                            Text(
                                text = "Nhập tên thương hiệu",
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                        singleLine = true
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Hiển thị:")
                            Switch(
                                checked = brandIsEnable.value,
                                onCheckedChange = {
                                    brandIsEnable.value = it
                                },
                                modifier = Modifier
                            )
                        }

                        if (isUpdate.value) {
                            Button(onClick = {
                                if (TextUtils.isEmpty(brandName.value.toString())) {
                                    Toast.makeText(context, "Bạn chưa nhập tên thương hiệu!!!", Toast.LENGTH_SHORT).show()
                                } else {
                                    isUploading.value = true
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            if (imageUrl == null) {
                                                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                                                val result = ImageCloudinary.uploadImage(context, bitmap)
                                                result.onSuccess { url ->
                                                    brandRepository.updateBrand(BrandModel(
                                                        id = brandId.value,
                                                        name = brandName.value,
                                                        imageUrl = url,
                                                        enable = brandIsEnable.value
                                                    ))
                                                }.onFailure { exception ->
                                                    Log.e("Cloudinary", "Không thể tải ảnh: ${exception.message}")
                                                }
                                            } else {
                                                brandRepository.updateBrand(BrandModel(
                                                    id = brandId.value,
                                                    name = brandName.value,
                                                    imageUrl = imageUrl.toString(),
                                                    enable = brandIsEnable.value
                                                ))
                                            }

                                            withContext(Dispatchers.Main) {
                                                brandList = brandRepository.getBrands()
                                                imageUri = null
                                                imageUrl = null
                                                brandName.value = ""
                                                brandIsEnable.value = true
                                                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                                                isUploading.value = false
                                                isUpdate.value = false
                                                brandId.value = ""
                                            }
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                isUploading.value = false
                                            }
                                        }
                                    }
                                }
                            }) {
                                Text(text = "Cập nhật")
                            }
                        } else {
                            Button(onClick = {
                                if (TextUtils.isEmpty(brandName.value.toString())) {
                                    Toast.makeText(context, "Bạn chưa nhập tên thương hiệu!!!", Toast.LENGTH_SHORT).show()
                                } else if (bitmap.value == null) {
                                    Toast.makeText(context, "Chọn ảnh thương hiệu", Toast.LENGTH_SHORT).show()
                                } else {
                                    isUploading.value = true
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                                            val result = ImageCloudinary.uploadImage(context, bitmap)
                                            result.onSuccess { url ->
                                                brandRepository.addBrand(BrandModel(
                                                    name = brandName.value,
                                                    imageUrl = url,
                                                    enable = true
                                                ))
                                            }.onFailure { exception ->
                                                Log.e("Cloudinary", "Không thể tải ảnh: ${exception.message}")
                                            }

                                            withContext(Dispatchers.Main) {
                                                brandList = brandRepository.getBrands()
                                                imageUri = null
                                                imageUrl = null
                                                brandName.value = ""
                                                brandIsEnable.value = true
                                                Toast.makeText(context, "Thêm thương hiệu thành công", Toast.LENGTH_SHORT).show()
                                                isUploading.value = false
                                            }
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                isUploading.value = false
                                            }
                                        }
                                    }
                                }
                            }) {
                                Text(text = "Thêm")
                            }
                        }
                    }
                }
            }
            if (isUploading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            items(brandList.size) { index ->
                brandItem(
                    brand = brandList[index],
                    onClickShowHidden = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                brandRepository.updateBrand(brandList[index].copy(enable = !brandList[index].enable))
                                brandList = brandRepository.getBrands()
                            } catch (e: Exception) {

                            }
                        }
                    },
                    onDeleteClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                brandRepository.deleteBrand(brandList[index])
                                ImageCloudinary.deleteImage(brandList[index].imageUrl)
                                brandList = brandRepository.getBrands()
                            } catch (e: Exception) {
                                Log.e("BannerScreen", "Delete failed: ${e.message}")
                            }
                        }
                    },
                    onClickItem = {
                        brandId.value = brandList[index].id
                        brandName.value = brandList[index].name
                        brandIsEnable.value = brandList[index].enable
                        imageUrl = brandList[index].imageUrl
                        imageUri = null
                        isUpdate.value = true
                    }
                )
            }
        }
    }
}


@Composable
fun brandItem(brand: BrandModel, onClickShowHidden: () -> Unit, onDeleteClick: () -> Unit, onClickItem: () -> Unit) {
    Card(
        onClick = {
            onClickItem()
        },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),

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
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = brand.name,
                modifier = Modifier.padding(top = 8.dp).padding(horizontal = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onClickShowHidden() }) {
                    Icon(painter = painterResource(id = if (brand.enable) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }

}