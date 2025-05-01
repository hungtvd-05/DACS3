package com.app_computer_ecom.dack.model

data class CardInfoModel(
    val selectType: PriceInfo = PriceInfo(),
    var quantity: Int = 0,
)
