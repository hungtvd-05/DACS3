package com.app_computer_ecom.dack.model

data class AddressModel private constructor(
    val id: String = "",
    val uid: String = "",
    val name: String = "",
    val phoneNum: String = "",
    val province: String = "",
    val district: String = "",
    val ward: String = "",
    val street: String = "",
    val default: Boolean = false
) {
    companion object {
        fun create(
            id: String = "",
            uid: String = "",
            name: String = "",
            phoneNum: String = "",
            province: String = "",
            district: String = "",
            ward: String = "",
            street: String = "",
            default: Boolean = false
        ): AddressModel {
            return AddressModel(id, uid, name, phoneNum, province, district, ward, street, default)
        }
    }

    constructor(): this("", "", "", "", "", "", "", "", false)
}
