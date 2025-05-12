package com.app_computer_ecom.dack.model

data class ImageInfo private constructor(
    var imageUrl: String = "",
    var isHidden: Boolean = false
) {
    companion object {
        fun create(imageUrl: String = "", isHidden: Boolean = false): ImageInfo {
            return ImageInfo(imageUrl, isHidden)
        }
    }

    constructor(): this("", false)
}