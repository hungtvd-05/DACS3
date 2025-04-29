package com.app_computer_ecom.dack.model

data class CartModel(
    val id: String = "",
    val uid: String = "",
    val pid: String = "",
    val selectType: PriceInfo = PriceInfo(),
    var quantity: Int = 0,
)
