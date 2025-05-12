package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class ProductModel private constructor(
    val id: String = "",
    var name: String = "",
    var categoryId: String = "",
    var brandId: String = "",
    var description: String = "",
    var prices: MutableList<PriceInfo> = mutableListOf(),
    var imageUrls: MutableList<ImageInfo> = mutableListOf(),
    val show: Boolean = true,
    val createdAt: Timestamp = Timestamp.now(),
    val numOfReviews: Int = 0,
    val numOfStars: Int = 0,
    val rating: Double = 0.0
) {
    companion object {
        fun create(
            id: String = "",
            name: String = "",
            categoryId: String = "",
            brandId: String = "",
            description: String = "",
            prices: MutableList<PriceInfo> = mutableListOf(),
            imageUrls: MutableList<ImageInfo> = mutableListOf(),
            show: Boolean = true,
            createdAt: Timestamp = Timestamp.now(),
            numOfReviews: Int = 0,
            numOfStars: Int = 0,
            rating: Double = 0.0
        ): ProductModel {
            return ProductModel(
                id,
                name,
                categoryId,
                brandId,
                description,
                prices,
                imageUrls,
                show,
                createdAt,
                numOfReviews,
                numOfStars,
                rating
            )
        }
    }

    constructor(): this("", "", "", "", "", mutableListOf(), mutableListOf(), true, Timestamp.now(), 0, 0, 0.0)
}
