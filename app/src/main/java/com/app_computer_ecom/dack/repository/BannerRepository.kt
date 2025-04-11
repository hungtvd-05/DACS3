package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.BannerModel

interface BannerRepository {
    suspend fun getBanners(): List<BannerModel>
    suspend fun getBannerByEnable(): List<BannerModel>
    suspend fun addBanner(banner: BannerModel)
    suspend fun updateBanner(banner: BannerModel)
    suspend fun deleteBanner(banner: BannerModel)
}