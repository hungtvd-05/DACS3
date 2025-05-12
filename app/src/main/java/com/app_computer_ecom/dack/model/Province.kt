package com.app_computer_ecom.dack.model

data class Province private constructor(
    val Id: String,
    val Name: String,
    val Districts: List<District>
) {
    companion object {
        fun create(
            Id: String,
            Name: String,
            Districts: List<District>
        ): Province {
            return Province(Id, Name, Districts)
        }
    }
}
