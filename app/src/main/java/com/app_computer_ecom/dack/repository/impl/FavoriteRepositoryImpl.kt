package com.app_computer_ecom.dack.repository.impl

import android.content.Context
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.FavoriteModel
import com.app_computer_ecom.dack.repository.FavoriteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class FavoriteRepositoryImpl : FavoriteRepository {

    var db = GlobalDatabase.database
    val dbFavorite: CollectionReference = db.collection("favorite")

    override suspend fun getFavoritesByUid(): List<FavoriteModel> {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return emptyList()
            val querySnapshot = dbFavorite
                .whereEqualTo("uid", uid)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { it.toObject(FavoriteModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun isFavorite(pid: String): Boolean {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return false
            val querySnapshot = dbFavorite
                .whereEqualTo("uid", uid)
                .whereEqualTo("pid", pid)
                .limit(1)
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addToFavorite(context: Context, productId: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        dbFavorite.add(FavoriteModel(uid = uid, pid = productId))
        AppUtil.showToast(context, "Thêm vào danh sách yêu thích thành công")
    }


    override suspend fun deleteFavorite(context: Context, productId: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        try {
            val querySnapshot = dbFavorite
                .whereEqualTo("uid", uid)
                .whereEqualTo("pid", productId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                dbFavorite.document(document.id).delete().await()
            }

            AppUtil.showToast(context, "Đã xoá khỏi danh sách yêu thích")
        } catch (e: Exception) {
            AppUtil.showToast(context, "Xoá thất bại: ${e.localizedMessage}")
        }
    }

}