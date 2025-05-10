package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class RatingModel(
    val id: String = "",
    val pid: String = "",
    val uid: String = "",
    val oid: String = "",
    val rating: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
    val commentModel: CommentModel? = null,
)
