package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class ProductModel(
    val id: String = "",
    var name: String = "",
    var categoryId: String = "",
    var brandId: String = "",
    var description: String = "",
    var prices: MutableList<PriceInfo> = mutableListOf(),
    var imageUrls: MutableList<ImageInfo> = mutableListOf(),
    val show: Boolean = true,
    val createdAt: Timestamp = Timestamp.now(),
)
