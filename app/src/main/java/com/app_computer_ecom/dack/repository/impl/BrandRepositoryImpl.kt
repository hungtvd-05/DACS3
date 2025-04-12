package com.app_computer_ecom.dack.repository.impl

import android.util.Log
import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.repository.BrandRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class BrandRepositoryImpl: BrandRepository {
    var db = GlobalDatabase.database
    val dbBrand: CollectionReference = db.collection("brands")

    override suspend fun getBrands(): List<BrandModel> {
        return try {
            val querySnapshot = dbBrand.get().await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(BrandModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getBrandById(id: Int): BrandModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getBrandByIsEnable(isEnable: Boolean): List<BrandModel> {
        return try {
            val querySnapshot = dbBrand.whereEqualTo("enable", true).get().await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(BrandModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addBrand(brand: BrandModel) {
        dbBrand.add(brand)
    }

    override suspend fun updateBrand(brand: BrandModel) {
        dbBrand.document(brand.id).set(brand)
    }

    override suspend fun deleteBrand(brand: BrandModel) {
        dbBrand.document(brand.id).delete()
    }
}