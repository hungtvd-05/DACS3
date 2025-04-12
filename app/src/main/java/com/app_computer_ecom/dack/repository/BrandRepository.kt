package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.BrandModel

interface BrandRepository {
    suspend fun getBrands(): List<BrandModel>
    suspend fun getBrandById(id: Int): BrandModel?
    suspend fun getBrandByIsEnable(isEnable: Boolean): List<BrandModel>
    suspend fun addBrand(brand: BrandModel)
    suspend fun updateBrand(brand: BrandModel)
    suspend fun deleteBrand(brand: BrandModel)
}