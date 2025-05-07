package com.app_computer_ecom.dack.repository.impl

import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.UserModel
import com.app_computer_ecom.dack.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {
    var db = GlobalDatabase.database
    var userdb: CollectionReference = db.collection("users")

    override suspend fun getUsers(): List<UserModel> {
        return try {
            val querySnapshot = userdb.get().await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(UserModel::class.java)?.copy(uid = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun updateUserRole(userModel: UserModel, role: String) {
        userdb.document(userModel.uid).update("role", role).await()
    }
}