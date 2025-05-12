package com.app_computer_ecom.dack.model

data class District private constructor(
    val Id: String,
    val Name: String,
    val Wards: List<Ward>
) {
    companion object {
        fun create(Id: String, Name: String, Wards: List<Ward>): District {
            return District(Id, Name, Wards)
        }
    }

    constructor(): this("", "", emptyList())
}
