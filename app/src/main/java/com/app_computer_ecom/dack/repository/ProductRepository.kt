package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.ProductModel

interface ProductRepository {
    suspend fun addProduct(product: ProductModel)
    suspend fun updateProduct(product: ProductModel)
    suspend fun deleteProduct(product: ProductModel)
    suspend fun getProducts(): List<ProductModel>
    suspend fun getProductById(productId: String): ProductModel?
    suspend fun getProductsByCreatedAt(): List<ProductModel>
    suspend fun getProductsByCategoryIdAndBrandId(categoryIds: List<String> = emptyList<String>(), brandIds: List<String> = emptyList<String>(), minPrice: Double = 0.0, maxPrice: Double = Double.MAX_VALUE): List<ProductModel>
}