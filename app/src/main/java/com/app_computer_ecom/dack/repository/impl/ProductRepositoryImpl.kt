package com.app_computer_ecom.dack.repository.impl

import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.ProductRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl: ProductRepository {
    var db = GlobalDatabase.database
    val dbProduct: CollectionReference = db.collection("products")

    override suspend fun addProduct(product: ProductModel) {
        dbProduct.add(product)
    }

    override suspend fun updateProduct(product: ProductModel) {
        dbProduct.document(product.id).set(product)
    }

    override suspend fun deleteProduct(product: ProductModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getProducts(): List<ProductModel> {
        return try {
            val querySnapshot = dbProduct.get().await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(ProductModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProductById(productId: String): ProductModel? {
        return try {
            val querySnapshot = dbProduct.document(productId).get().await()
            if (!querySnapshot.exists()) {
                null
            } else {
                querySnapshot.toObject(ProductModel::class.java)?.copy(id = productId)
            }
        } catch (e: Exception) {
            null
        }
    }

}