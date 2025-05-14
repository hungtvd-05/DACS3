package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.ProductModel

interface ProductRepository {
    suspend fun addProduct(product: ProductModel)
    suspend fun updateProduct(product: ProductModel)
    suspend fun showHiddenProduct(product: ProductModel)
    suspend fun deleteProduct(product: ProductModel)
    suspend fun getProducts(): List<ProductModel>
    suspend fun getProductsWithFilter(
        searchQuery: String,
        categoryIds: List<String> = emptyList<String>(),
        brandIds: List<String> = emptyList<String>(),
        minPrice: Int = 0,
        maxPrice: Int = Int.MAX_VALUE,
        limit: Int = Int.MAX_VALUE,
        rating: Int = 0,
        isEnable: Int = 1
    ): List<ProductModel>

    suspend fun getProductById(productId: String): ProductModel?
    suspend fun getProductsByCreatedAt(): List<ProductModel>
    suspend fun getProductsByCategoryIdAndBrandId(
        categoryIds: List<String> = emptyList<String>(),
        brandIds: List<String> = emptyList<String>(),
        minPrice: Int = 0,
        maxPrice: Int = Int.MAX_VALUE,
        limit: Int = Int.MAX_VALUE,
        rating: Int = 0,
        isEnable: Int = 1
    ): List<ProductModel>
}