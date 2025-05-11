package com.app_computer_ecom.dack.repository.impl

import android.util.Log
import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.model.ProductModel
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

    override suspend fun getCategorybyId(categoryId: String): CategoryModel? {
        return try {
            val querySnapshot = dbCategory.document(categoryId).get().await()
            if (!querySnapshot.exists()) {
                null
            } else {
                querySnapshot.toObject(CategoryModel::class.java)?.copy(id = categoryId)
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCategorybyIsEnable(): List<CategoryModel> {
        return try {
            val querySnapshot = dbCategory.whereEqualTo("enable", true).get().await()
            if (querySnapshot.isEmpty) {
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