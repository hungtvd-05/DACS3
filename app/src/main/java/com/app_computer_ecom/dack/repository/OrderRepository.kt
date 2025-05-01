package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.OrderModel

interface OrderRepository {
    suspend fun addOrder(orderModel: OrderModel)
}