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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.admin.HeaderViewMenu
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.repository.CategoryRepository
import com.app_computer_ecom.dack.repository.impl.CategoryRepositoryImpl
import com.app_computer_ecom.dack.viewmodel.ImageCloudinary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CategoryScreen(navController: NavHostController, modifier: Modifier = Modifier) {

    val categoryRepository: CategoryRepository = remember { CategoryRepositoryImpl() }
    val context = LocalContext.current
    BackHandler(enabled = true) {
        navController.navigate("admin/01") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    var categoryList by remember { mutableStateOf(emptyList<CategoryModel>()) }

    var categoryId = remember { mutableStateOf("") }
    var categoryName = remember { mutableStateOf("") }
    var categoryIsEnable = remember { mutableStateOf(true) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var isUploading = remember { mutableStateOf(false) }
    var isUpdate = remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        imageUrl = null
    }

    LaunchedEffect(Unit) {
        categoryList = categoryRepository.getCategories()
        isLoading = false
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
        modifier = Modifier
            .fillMaxSize()
    ) {
        HeaderViewMenu(
            title = "Category",
            onBackClick = { navController.navigate("admin/01") },
            onAddClick = {
                imageUri = null
                imageUrl = null
                categoryName.value = ""
                categoryIsEnable.value = true
                categoryId.value = ""
                isUpdate.value = false
            },
            isUploading = false
        )
//        Spacer(modifier = Modifier.height(10.dp))

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
                            Text("Image")
                        }
                    }
                }
                Column {
                    OutlinedTextField(
                        value = categoryName.value,
                        onValueChange = { categoryName.value = it },
                        label = {
                            Text(
                                text = "Enter category's name",
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
                            Text(text = "Show category:")
                            Switch(
                                checked = categoryIsEnable.value,
                                onCheckedChange = {
                                    categoryIsEnable.value = it
                                },
                                modifier = Modifier
                            )
                        }

                        if (isUpdate.value) {
                            Button(onClick = {
                                if (TextUtils.isEmpty(categoryName.value.toString())) {
                                    Toast.makeText(context, "Please enter category's name", Toast.LENGTH_SHORT).show()
                                } else {
                                    isUploading.value = true
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            if (imageUrl == null) {
                                                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                                                val result = ImageCloudinary.uploadImage(context, bitmap)
                                                result.onSuccess { url ->
                                                    categoryRepository.updateCategory(CategoryModel(
                                                        id = categoryId.value,
                                                        name = categoryName.value,
                                                        imageUrl = url,
                                                        enable = categoryIsEnable.value
                                                    ))
                                                }.onFailure { exception ->
                                                    Log.e("Cloudinary", "Upload failed for one image: ${exception.message}")
                                                }
                                            } else {
                                                categoryRepository.updateCategory(CategoryModel(
                                                    id = categoryId.value,
                                                    name = categoryName.value,
                                                    imageUrl = imageUrl.toString(),
                                                    enable = categoryIsEnable.value
                                                ))
                                            }

                                            withContext(Dispatchers.Main) {
                                                categoryList = categoryRepository.getCategories()
                                                imageUri = null
                                                imageUrl = null
                                                categoryName.value = ""
                                                categoryIsEnable.value = true
                                                Toast.makeText(context, "Add success", Toast.LENGTH_SHORT).show()
                                                isUploading.value = false
                                                isUpdate.value = false
                                                categoryId.value = ""
                                            }
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                isUploading.value = false
                                            }
                                        }
                                    }
                                }
                            }) {
                                Text(text = "Update")
                            }
                        } else {
                            Button(onClick = {
                                if (TextUtils.isEmpty(categoryName.value.toString())) {
                                    Toast.makeText(context, "Please enter category's name", Toast.LENGTH_SHORT).show()
                                } else if (bitmap.value == null) {
                                    Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                                } else {
                                    isUploading.value = true
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                                            val result = ImageCloudinary.uploadImage(context, bitmap)
                                            result.onSuccess { url ->
                                                categoryRepository.addCategory(CategoryModel(
                                                    name = categoryName.value,
                                                    imageUrl = url,
                                                    enable = true
                                                ))
                                            }.onFailure { exception ->
                                                Log.e("Cloudinary", "Upload failed for one image: ${exception.message}")
                                            }

                                            withContext(Dispatchers.Main) {
                                                categoryList = categoryRepository.getCategories()
                                                imageUri = null
                                                imageUrl = null
                                                categoryName.value = ""
                                                categoryIsEnable.value = true
                                                Toast.makeText(context, "Add success", Toast.LENGTH_SHORT).show()
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
                                Text(text = "Add")
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

        if (isLoading) {
            LoadingScreen()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                items(categoryList.size) { index ->
                    categoryItem(
                        category = categoryList[index],
                        onClickShowHidden = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    categoryRepository.updateCategory(categoryList[index].copy(enable = !categoryList[index].enable))
                                    categoryList = categoryRepository.getCategories()
                                } catch (e: Exception) {

                                }
                            }
                        },
                        onDeleteClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    categoryRepository.deleteCategory(categoryList[index])
                                    ImageCloudinary.deleteImage(categoryList[index].imageUrl)
                                    categoryList = categoryRepository.getCategories()
                                } catch (e: Exception) {
                                    Log.e("BannerScreen", "Delete failed: ${e.message}")
                                }
                            }
                        },
                        onClickItem = {
                            categoryId.value = categoryList[index].id
                            categoryName.value = categoryList[index].name
                            categoryIsEnable.value = categoryList[index].enable
                            imageUrl = categoryList[index].imageUrl
                            imageUri = null
                            isUpdate.value = true
                        }
                    )
                }
                item(span = { GridItemSpan(3) }) {

                }
            }
        }
    }
}

@Composable
fun categoryItem(category: CategoryModel, onClickShowHidden: () -> Unit, onDeleteClick: () -> Unit, onClickItem: () -> Unit) {
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
                model = category.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = category.name,
                modifier = Modifier.padding(top = 8.dp).padding(horizontal = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onClickShowHidden() }) {
                    Icon(painter = painterResource(id = if (category.enable) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp),
                        tint = Color(30, 136, 229)
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