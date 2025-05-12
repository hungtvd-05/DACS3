package com.app_computer_ecom.dack.model

data class FavoriteModel private constructor(
    val id: String = "",
    val uid: String = "",
    val pid: String = ""
) {
    companion object {
        fun create(
            id: String = "",
            uid: String = "",
            pid: String = ""
        ): FavoriteModel {
            return FavoriteModel(id, uid, pid)
        }
    }

    constructor(): this("", "", "")
}
