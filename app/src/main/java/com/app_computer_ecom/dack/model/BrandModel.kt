package com.app_computer_ecom.dack.model

data class BrandModel private constructor (
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val enable: Boolean = true,
) {
    companion object {
        fun create(id: String = "", name: String = "", imageUrl: String = "", enable: Boolean = true): BrandModel {
            return BrandModel(id, name, imageUrl, enable)
        }
    }
    constructor() : this("", "", "", true)
}
