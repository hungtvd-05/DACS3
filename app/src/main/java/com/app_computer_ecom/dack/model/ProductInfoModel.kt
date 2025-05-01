package com.app_computer_ecom.dack.model

data class ProductInfoModel (
    val id: String = "",
    var name: String = "",
    var imageUrl: String = "",
    val selectType: PriceInfo = PriceInfo(),
    val quantity: Int = 0,
)