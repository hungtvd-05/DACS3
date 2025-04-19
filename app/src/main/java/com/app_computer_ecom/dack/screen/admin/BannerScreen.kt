package com.app_computer_ecom.dack.screen.admin

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app_computer_ecom.dack.components.ImagePreviewItem
import com.app_computer_ecom.dack.components.admin.HeaderViewMenu
import com.app_computer_ecom.dack.model.BannerModel
import com.app_computer_ecom.dack.repository.BannerRepository
import com.app_computer_ecom.dack.repository.impl.BannerRepositoryImpl
import com.app_computer_ecom.dack.viewmodel.ImageCloudinary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BannerScreen(navController: NavHostController, modifier: Modifier = Modifier) {

    val bannerRepository: BannerRepository = remember { BannerRepositoryImpl() }

    val context = LocalContext.current
    val isUploading = remember { mutableStateOf(false) }

    var banners by remember {
        mutableStateOf(emptyList<BannerModel>())
    }

    BackHandler(enabled = true) {
        navController.navigate("admin/01") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    LaunchedEffect(Unit) {
        banners = bannerRepository.getBanners()
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
                            bannerRepository.addBanner(BannerModel(imageUrl = url))
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
        HeaderViewMenu(
            title = "Banner",
            onBackClick = {navController.navigate("admin/1")},
            onAddClick = {launcher.launch("image/*")},
            isUploading = isUploading.value
        )

        LazyColumn {
            items(banners.size) { index ->
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