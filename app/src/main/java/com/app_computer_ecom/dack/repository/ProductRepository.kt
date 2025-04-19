package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.ProductModel

interface ProductRepository {
    suspend fun addProduct(product: ProductModel)
    suspend fun updateProduct(product: ProductModel)
    suspend fun deleteProduct(product: ProductModel)
    suspend fun getProducts(): List<ProductModel>
    suspend fun getProductById(productId: String): ProductModel?
}