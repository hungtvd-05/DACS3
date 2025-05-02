package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.OrderModel
import com.google.firebase.Timestamp

interface OrderRepository {
    suspend fun addOrder(orderModel: OrderModel)
    suspend fun getOrdersOnAdmin(): List<OrderModel>
    suspend fun getOrderById(orderId: String): OrderModel?
    suspend fun updateOrderStatus(order: OrderModel, newStatus: Int, finishedAt: Timestamp = Timestamp.now())

}