package com.app_computer_ecom.dack.model

data class ProductModel(
    val id: String = "",
    var name: String = "",
    var categoryId: String = "",
    var brandId: String = "",
    var description: String = "",
    var prices: MutableList<PriceInfo> = mutableListOf(),
    var imageUrls: MutableList<ImageInfo> = mutableListOf(),
    val show: Boolean = true
)
