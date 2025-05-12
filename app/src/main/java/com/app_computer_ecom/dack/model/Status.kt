package com.app_computer_ecom.dack.model

data class Status private constructor(
    val id: Int,
    val title: String,
    val idPainterResource: Int
) {
    companion object {
        fun create(id: Int, title: String, idPainterResource: Int): Status {
            return Status(id, title, idPainterResource)
        }
    }

    constructor(): this(0, "", 0)
}
