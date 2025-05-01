package com.app_computer_ecom.dack.repository.impl

import android.util.Log
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.repository.OrderRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class OrderRepositoryImpl : OrderRepository {
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val dbOrder: CollectionReference = db.collection("orders")

    override suspend fun addOrder(orderModel: OrderModel) {
        dbOrder.add(orderModel)
        GlobalRepository.cartRepository.clearCart()
    }
}