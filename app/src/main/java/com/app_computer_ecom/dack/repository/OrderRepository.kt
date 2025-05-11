package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.DailySales
import com.app_computer_ecom.dack.model.MonthlySales
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.model.ProductInfoModel
import com.app_computer_ecom.dack.model.ProductSoldInfo
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun addOrder(orderModel: OrderModel)
    suspend fun getOrdersOnAdmin(): Flow<List<OrderModel>>
    suspend fun getOrderById(orderId: String): OrderModel?
    suspend fun updateOrderStatus(order: OrderModel, newStatus: Int, finishedAt: Timestamp = Timestamp.now())
    suspend fun updateOrderListProduct(orderId: String, listProduct: List<ProductInfoModel>)
    suspend fun getDailySalesCurrentMonth(): List<DailySales>
    suspend fun getMonthlySalesCurrentYear(): List<MonthlySales>
    suspend fun getOrdersByUidAndStatus(uid: String, status: Int): List<OrderModel>
    suspend fun getProductSoldQuantities(): List<ProductSoldInfo>
}