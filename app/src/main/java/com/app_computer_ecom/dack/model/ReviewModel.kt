package com.app_computer_ecom.dack.model

data class ReviewModel(
    val id: Int,
    val pid: String,
    val uid: String,
    val rating: Int,
    val imageUrls: MutableList<ImageInfo> = mutableListOf(),
    val replies: MutableList<ReviewModel> = mutableListOf()
)
