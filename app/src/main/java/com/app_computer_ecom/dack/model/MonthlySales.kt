package com.app_computer_ecom.dack.model

data class MonthlySales(
    val month: String, // Định dạng: "Tháng 1"
    val totalOrders: Double,
    val totalOrdersCompleted: Double,
    val totalExpectedSales: Double,
    val totalAchievedSales: Double
)
