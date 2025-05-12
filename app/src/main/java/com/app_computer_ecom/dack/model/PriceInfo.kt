package com.app_computer_ecom.dack.model

data class PriceInfo private constructor(
    val id: String = "",
    val type: String = "",
    val price: Int = 0,
    val quantity: Int = 0,
    val sold: Int = 0,
) {
    companion object {
        fun create(
            id: String = "",
            type: String = "",
            price: Int = 0,
            quantity: Int = 0,): PriceInfo {
            return PriceInfo(id, type, price, quantity)
        }
    }

    constructor(): this("", "", 0, 0)
}
