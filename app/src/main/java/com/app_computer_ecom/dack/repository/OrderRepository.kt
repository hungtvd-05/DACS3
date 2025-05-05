package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.DailySales
import com.app_computer_ecom.dack.model.MonthlySales
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.model.ProductSoldInfo
import com.google.firebase.Timestamp

interface OrderRepository {
    suspend fun addOrder(orderModel: OrderModel)
    suspend fun getOrdersOnAdmin(): List<OrderModel>
    suspend fun getOrderById(orderId: String): OrderModel?
    suspend fun updateOrderStatus(order: OrderModel, newStatus: Int, finishedAt: Timestamp = Timestamp.now())
    suspend fun getDailySalesLast6Days(): List<DailySales>
    suspend fun getDailySalesLast6Months(): List<MonthlySales>
    suspend fun getOrdersByUidAndStatus(uid: String, status: Int): List<OrderModel>
    suspend fun getProductSoldQuantities(): List<ProductSoldInfo>
}