package com.app_computer_ecom.dack.repository.impl

import android.util.Log
import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.repository.OrderRepository
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl : OrderRepository {
    var db = GlobalDatabase.database

    val dbOrder: CollectionReference = db.collection("orders")

    override suspend fun getOrdersByUidAndStatus(uid: String, status: Int): List<OrderModel> {
        return try {
            val querySnapshot = dbOrder
                .whereEqualTo("uid", uid)
                .whereEqualTo("status", status)
                .get()
                .await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(OrderModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            Log.e("OrderRepositoryImpl", "Error fetching orders: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun addOrder(orderModel: OrderModel) {
        dbOrder.add(orderModel)
        GlobalRepository.cartRepository.clearCart()
    }

    override suspend fun getOrdersOnAdmin(): List<OrderModel> {
        return try {
            val querySnapshot =
                dbOrder.orderBy("createdAt", Query.Direction.DESCENDING).get().await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(OrderModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getOrderById(orderId: String): OrderModel? {
        return try {
            val querySnapshot = dbOrder.document(orderId).get().await()
            if (!querySnapshot.exists()) {
                null
            } else {
                querySnapshot.toObject(OrderModel::class.java)?.copy(id = orderId)
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateOrderStatus(
        order: OrderModel,
        newStatus: Int,
        finishedAt: Timestamp
    ) {
        dbOrder.document(order.id).update("status", newStatus).await()
        val batch = Firebase.firestore.batch()
        batch.update(dbOrder.document(order.id), "finishedAt", finishedAt)
        if (newStatus == 3) {
            order.listProduct.forEach { productInfoModel ->
                var product =
                    GlobalRepository.productRepository.getProductById(productInfoModel.id) ?: run {
                        return@forEach
                    }
                val priceInfo =
                    product.prices.find { it.id == productInfoModel.selectType.id } ?: run {
                        return@forEach
                    }
                val newSold = priceInfo.sold + productInfoModel.quantity
                val newQuantity = priceInfo.quantity - productInfoModel.quantity
                if (newQuantity < 0) {
                    return@forEach
                }
                val priceIndex = product.prices.indexOf(priceInfo)
                product.prices[priceIndex] = priceInfo.copy(quantity = newQuantity, sold = newSold)

                val productRef = GlobalDatabase.database.collection("products").document(product.id)
                batch.set(productRef, product)
            }
        }
        batch.commit().await()
    }


}