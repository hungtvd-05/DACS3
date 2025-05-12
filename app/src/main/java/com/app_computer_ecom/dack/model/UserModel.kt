package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class UserModel private constructor(
    val name: String = "",
    val username: String = "",
    val address: String = "",
    val avatar: String = "",
    val email: String = "",
    val role: String = "",
    val uid: String = "",
    val birthDate: Timestamp = Timestamp.now(),
    val sex: String = "",
    val phoneNumber: String = "",
) {
    companion object {
        fun create(
            name: String = "",
            username: String = "",
            address: String = "",
            avatar: String = "",
            email: String = "",
            role: String = "",
            uid: String = "",
            birthDate: Timestamp = Timestamp.now(),
            sex: String = "",
            phoneNumber: String = "",
        ): UserModel {
            return UserModel(
                name = name,
                username = username,
                address = address,
                avatar = avatar,
                email = email,
                role = role,
                uid = uid,
                birthDate = birthDate,
                sex = sex,
                phoneNumber = phoneNumber,
            )
        }
    }

    constructor(): this("", "", "", "", "", "", "", Timestamp.now(), "", "")
}
