package com.app_computer_ecom.dack.model

data class ProductInfoModel private constructor(
    val id: String = "",
    var name: String = "",
    var imageUrl: String = "",
    val selectType: PriceInfo = PriceInfo(),
    val quantity: Int = 0,
    val ratingId: String = "",
    val ratingStar: Int = 0,
) {
    companion object {
        fun create(
            id: String = "",
            name: String = "",
            imageUrl: String = "",
            selectType: PriceInfo = PriceInfo(),
            quantity: Int = 0,
            ratingId: String = "",
            ratingStar: Int = 0,
        ): ProductInfoModel {
            return ProductInfoModel(id, name, imageUrl, selectType, quantity, ratingId, ratingStar)
        }
    }

    constructor(): this("", "", "", PriceInfo(), 0, "", 0)
}