package com.app_computer_ecom.dack.model

data class CartModel private constructor(
    val id: String = "",
    val uid: String = "",
    val pid: String = "",
    val selectType: PriceInfo = PriceInfo(),
    var quantity: Int = 0,
) {
    companion object {
        fun create(
            id: String = "",
            uid: String = "",
            pid: String = "",
            selectType: PriceInfo = PriceInfo(),
            quantity: Int = 0): CartModel {
            return CartModel(id, uid, pid, selectType, quantity)
        }
    }

    constructor(): this("", "", "", PriceInfo(), 0)
}
