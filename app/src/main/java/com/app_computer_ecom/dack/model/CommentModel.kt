package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class CommentModel private constructor(
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val imageUrls: MutableList<String> = mutableListOf(),
    val reply: String = "",
) {
    companion object {
        fun create(
            content: String = "",
            createdAt: Timestamp = Timestamp.now(),
            imageUrls: MutableList<String> = mutableListOf(),
            reply: String = "",
        ): CommentModel {
            return CommentModel(content, createdAt, imageUrls, reply)
        }
    }

    constructor(): this("", Timestamp.now(), mutableListOf(), "")
}
