package com.app_computer_ecom.dack.repository.impl

import android.util.Log
import com.app_computer_ecom.dack.model.BannerModel
import com.app_computer_ecom.dack.repository.BannerRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BannerRepositoryImpl : BannerRepository {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val dbBanner: CollectionReference = db.collection("banners")

    override suspend fun getBanners(): List<BannerModel> {
        return try {
            val querySnapshot = dbBanner.get().await()
            if (querySnapshot.isEmpty) {
                Log.d("BannerRepositoryImpl", "No banners found")
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(BannerModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            Log.e("BannerRepositoryImpl", "Error fetching banners: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getBannerByEnable(): List<BannerModel> {
        return try {
            val querySnapshot = dbBanner.whereEqualTo("show", true).get().await()
            if (querySnapshot.isEmpty) {
                Log.d("BannerRepositoryImpl", "No banners found")
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(BannerModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            Log.e("BannerRepositoryImpl", "Error fetching banners: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun addBanner(banner: BannerModel) {
        dbBanner.add(banner)
    }

    override suspend fun updateBanner(banner: BannerModel) {
        dbBanner.document(banner.id).set(banner)
    }

    override suspend fun deleteBanner(banner: BannerModel) {
        dbBanner.document(banner.id).delete()
    }

}