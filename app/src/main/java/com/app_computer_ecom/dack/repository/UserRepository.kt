package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.UserModel

interface UserRepository {
    suspend fun getUsers(): List<UserModel>
    suspend fun updateUserRole(userModel: UserModel, role: String)
}