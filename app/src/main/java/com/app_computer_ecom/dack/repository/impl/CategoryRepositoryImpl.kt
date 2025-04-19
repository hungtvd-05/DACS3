package com.app_computer_ecom.dack.repository.impl

import android.util.Log
import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.BannerModel
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.repository.CategoryRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class CategoryRepositoryImpl : CategoryRepository {
    var db = GlobalDatabase.database
    val dbCategory: CollectionReference = db.collection("categories")

    override suspend fun getCategories(): List<CategoryModel> {
        return try {
            val querySnapshot = dbCategory.get().await()
            if (querySnapshot.isEmpty) {
                Log.d("BannerRepositoryImpl", "No banners found")
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(CategoryModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCategorybyName(): CategoryModel {
        TODO("Not yet implemented")
    }

    override suspend fun getCategorybyId(): CategoryModel {
        TODO("Not yet implemented")
    }

    override suspend fun getCategorybyIsEnable(): List<CategoryModel> {
        return try {
            val querySnapshot = dbCategory.whereEqualTo("show", true).get().await()
            if (querySnapshot.isEmpty) {
                Log.d("BannerRepositoryImpl", "No banners found")
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(CategoryModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addCategory(category: CategoryModel) {
        dbCategory.add(category)
    }

    override suspend fun updateCategory(category: CategoryModel) {
        dbCategory.document(category.id).set(category)
    }

    override suspend fun deleteCategory(category: CategoryModel) {
        dbCategory.document(category.id).delete()
    }

}