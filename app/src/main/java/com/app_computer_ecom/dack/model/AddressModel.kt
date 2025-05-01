package com.app_computer_ecom.dack.model

data class AddressModel(
    val id: String = "",
    val uid: String = "",
    val name: String = "",
    val phoneNum: String = "",
    val province: String = "",
    val district: String = "",
    val ward: String = "",
    val street: String = "",
    val default: Boolean = false
)
