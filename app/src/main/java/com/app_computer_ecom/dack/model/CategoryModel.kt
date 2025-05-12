package com.app_computer_ecom.dack.model

data class CategoryModel private constructor (
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val enable: Boolean = true,
) {
    companion object {
        fun create(
            id: String = "",
            name: String = "",
            imageUrl: String = "",
            enable: Boolean = true): CategoryModel {
            return CategoryModel(id, name, imageUrl, enable)
        }
    }

    constructor(): this("", "", "", true)
}
