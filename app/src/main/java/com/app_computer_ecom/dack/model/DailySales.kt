package com.app_computer_ecom.dack.model

data class DailySales private constructor(
    val date: String,
    val totalOrders: Double,
    val totalOrdersCompleted: Double,
    val totalOrdersCanceled: Double,
    val totalExpectedSales: Double,
    val totalAchievedSales: Double
) {
    companion object {
        fun create(
            date: String,
            totalOrders: Double,
            totalOrdersCompleted: Double,
            totalOrdersCanceled: Double,
            totalExpectedSales: Double,
            totalAchievedSales: Double
            ): DailySales {
            return DailySales(
                date = date,
                totalOrders = totalOrders,
                totalOrdersCompleted = totalOrdersCompleted,
                totalOrdersCanceled = totalOrdersCanceled,
                totalExpectedSales = totalExpectedSales,
                totalAchievedSales = totalAchievedSales
            )
        }
    }

    constructor(): this("", 0.0, 0.0, 0.0, 0.0, 0.0)
}
