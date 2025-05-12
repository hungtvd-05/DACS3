package com.app_computer_ecom.dack.model

data class BannerModel private constructor(
    val id: String = "",
    val imageUrl: String = "",
    val enable: Boolean = true,
) {
    companion object {
        fun create(id: String = "", imageUrl: String = "", enable: Boolean = true): BannerModel {
            return BannerModel(id, imageUrl, enable)
        }
    }

    constructor() : this("", "", true)
}
