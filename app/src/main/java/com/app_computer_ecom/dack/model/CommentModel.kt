package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class CommentModel(
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val imageUrls: MutableList<String> = mutableListOf(),
    val reply: String = "",
)
