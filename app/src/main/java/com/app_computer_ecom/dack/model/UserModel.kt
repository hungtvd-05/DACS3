package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class UserModel(
    val name: String = "",
    val username: String = "",
    val avatar: String = "",
    val email: String = "",
    val role: String = "",
    val uid: String = "",
    val birthDate: Timestamp = Timestamp.now(),
)
