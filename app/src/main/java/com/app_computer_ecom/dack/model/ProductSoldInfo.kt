package com.app_computer_ecom.dack.model

data class ProductSoldInfo private constructor(
    val id: String = "",
    val name: String = "",
    val totalSold: Int = 0,
    val revenue: Long = 0
) {
    companion object {
        fun create(
            id: String = "",
            name: String = "",
            totalSold: Int = 0,
            revenue: Long = 0
        ): ProductSoldInfo {
            return ProductSoldInfo(
                id = id,
                name = name,
                totalSold = totalSold,
                revenue = revenue
            )
        }
    }

    constructor(): this("", "", 0, 0)
}
