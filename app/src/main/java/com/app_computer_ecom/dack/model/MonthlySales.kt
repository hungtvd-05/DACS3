package com.app_computer_ecom.dack.model

data class MonthlySales private constructor(
    val month: String, // Định dạng: "Tháng 1"
    val totalOrders: Double,
    val totalOrdersCompleted: Double,
    val totalExpectedSales: Double,
    val totalAchievedSales: Double
) {
    companion object {
        fun create(
            month: String,
            totalOrders: Double,
            totalOrdersCompleted: Double,
            totalExpectedSales: Double,
            totalAchievedSales: Double
        ): MonthlySales {
            return MonthlySales(month, totalOrders, totalOrdersCompleted, totalExpectedSales, totalAchievedSales)
        }
    }

    constructor() : this("", 0.0, 0.0, 0.0, 0.0)
}
