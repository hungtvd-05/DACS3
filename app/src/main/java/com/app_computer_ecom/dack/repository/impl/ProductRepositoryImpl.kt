package com.app_computer_ecom.dack.repository.impl

import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.repository.ProductRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


class ProductRepositoryImpl : ProductRepository {
    private var cachedProducts: List<ProductModel>? = null
    var db = GlobalDatabase.database
    val dbProduct: CollectionReference = db.collection("products")


    override suspend fun addProduct(product: ProductModel) {
        dbProduct.add(product)
    }

    override suspend fun updateProduct(product: ProductModel) {
        dbProduct.document(product.id).set(product)
    }

    override suspend fun showHiddenProduct(product: ProductModel) {
        GlobalRepository.cartRepository.deleteCartByPid(product.id)
        dbProduct.document(product.id).set(product.copy(show = !product.show))
    }

    override suspend fun deleteProduct(product: ProductModel) {
        GlobalRepository.cartRepository.deleteCartByPid(product.id)
        dbProduct.document(product.id).delete()
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

    override suspend fun getProductsByCreatedAt(): List<ProductModel> {
        return try {
            val querySnapshot =
                dbProduct.orderBy("createdAt", Query.Direction.DESCENDING).limit(20).get().await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(ProductModel::class.java)?.copy(id = document.id)
                        ?.takeIf { it.show }
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProductsByCategoryIdAndBrandId(
        categoryIds: List<String>,
        brandIds: List<String>,
        minPrice: Int,
        maxPrice: Int,
        limit: Long
    ): List<ProductModel> {
        return try {
            val querySnapshot =
                dbProduct.orderBy("createdAt", Query.Direction.DESCENDING).limit(limit).get()
                    .await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(ProductModel::class.java)?.copy(id = document.id)?.takeIf {
                        it.show
                    }.takeIf {
                        categoryIds.isEmpty() || categoryIds.contains(it?.categoryId)
                    }.takeIf {
                        brandIds.isEmpty() || brandIds.contains(it?.brandId)
                    }?.takeIf {
                        it.prices.any { priceInfo ->
                            val price = (priceInfo.price as? Number)?.toInt() ?: 0
                            price in minPrice..maxPrice
                        }
                    }
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProductsWithFilter(
        searchQuery: String,
        categoryIds: List<String>,
        brandIds: List<String>,
        minPrice: Int,
        maxPrice: Int,
        limit: Int
    ): List<ProductModel> {
        return try {
            loadProductsCacheIfNeeded()

            val trimmedQuery = searchQuery.trim().lowercase()

            val filteredProducts = cachedProducts.orEmpty().filter { product ->
                product.show &&
                        (trimmedQuery.isEmpty() || product.name.lowercase()
                            .contains(trimmedQuery)) &&
                        (categoryIds.isEmpty() || categoryIds.contains(product.categoryId)) &&
                        (brandIds.isEmpty() || brandIds.contains(product.brandId)) &&
                        (product.prices.any { priceInfo ->
                            val price = (priceInfo.price as? Number)?.toInt() ?: 0
                            price in minPrice..maxPrice
                        })
            }

            val limitedProducts = filteredProducts.take(limit)

            limitedProducts
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun loadProductsCacheIfNeeded() {
        if (cachedProducts == null) {
            val querySnapshot = dbProduct
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            cachedProducts = querySnapshot.documents.mapNotNull { document ->
                document.toObject(ProductModel::class.java)
                    ?.copy(id = document.id)
            }
        }
    }


}