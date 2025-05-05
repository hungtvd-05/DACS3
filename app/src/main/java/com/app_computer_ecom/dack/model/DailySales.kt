package com.app_computer_ecom.dack.model

data class DailySales(
    val date: String,
    val totalOrders: Double,
    val totalOrdersCompleted: Double,
    val totalExpectedSales: Double,
    val totalAchievedSales: Double
)
