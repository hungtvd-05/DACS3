package com.app_computer_ecom.dack.pages.admin

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.components.ImagePreviewItem
import com.app_computer_ecom.dack.model.BannerModel
import com.app_computer_ecom.dack.repository.BannerRepository
import com.app_computer_ecom.dack.repository.impl.BannerRepositoryImpl
import com.app_computer_ecom.dack.viewmodel.ImageCloudinary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BannerPage(modifier: Modifier = Modifier) {

    val bannerRepository: BannerRepository = remember { BannerRepositoryImpl() }

    val context = LocalContext.current
    val isUploading = remember { mutableStateOf(false) }

    var banners by remember {
        mutableStateOf(emptyList<BannerModel>())
    }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        banners = bannerRepository.getBanners()
        isLoading = false
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        uris?.let { uriList ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    isUploading.value = true

                    uriList.forEach { uri ->
                        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        val result = ImageCloudinary.uploadImage(context, bitmap)
                        result.onSuccess { url ->
                            bannerRepository.addBanner(BannerModel.create(imageUrl = url))
                        }.onFailure { exception ->
                            Log.e("Cloudinary", "Upload failed for one image: ${exception.message}")
                        }
                    }

                    withContext(Dispatchers.Main) {
                        banners = bannerRepository.getBanners()
                        isUploading.value = false
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("Cloudinary", "Upload failed: ${e.message}")
                        isUploading.value = false
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            if (banners.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Không có banner nào", fontSize = 12.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                ) {
                    items(banners.size) { index ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            ImagePreviewItem(
                                uri = banners[index].imageUrl,
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            ImageCloudinary.deleteImage(banners[index].imageUrl)
                                            bannerRepository.deleteBanner(banners[index])
                                            banners = bannerRepository.getBanners()
                                        } catch (e: Exception) {
                                            Log.e("BannerScreen", "Delete failed: ${e.message}")
                                        }
                                    }
                                },
                                onClickShowHidden = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            bannerRepository.updateBanner(banners[index].copy(enable = !banners[index].enable))
                                            banners = bannerRepository.getBanners()
                                        } catch (e: Exception) {

                                        }
                                    }
                                },
                                isEnable = banners[index].enable,
                                modifier = Modifier.fillMaxWidth().height(120.dp)
                            )
                        }

                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Button(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Thêm banner",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

    }
}