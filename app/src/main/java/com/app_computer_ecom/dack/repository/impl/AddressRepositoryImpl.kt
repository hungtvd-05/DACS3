package com.app_computer_ecom.dack.repository.impl

import android.util.Log
import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.AddressModel
import com.app_computer_ecom.dack.model.BannerModel
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.AddressRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class AddressRepositoryImpl : AddressRepository {
    var db: FirebaseFirestore = GlobalDatabase.database

    val dbAddress: CollectionReference = db.collection("addresses")

    override suspend fun addAddress(address: AddressModel) {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: throw IllegalStateException()
            val batch = Firebase.firestore.batch()

            if (address.default) {
                val querySnapshot = dbAddress.whereEqualTo("uid", uid).get().await()
                for (document in querySnapshot.documents) {
                    batch.update(dbAddress.document(document.id), "default", false)
                }
            }

            val addressId = address.id.ifEmpty { dbAddress.document().id }
            batch.set(dbAddress.document(addressId), address.copy(id = addressId, uid = uid))
            batch.commit().await()
        } catch (e: Exception) {

        }
    }

    override suspend fun updateAddress(address: AddressModel) {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: throw IllegalStateException()
            val batch = Firebase.firestore.batch()
            if (address.default) {
                val querySnapshot = dbAddress.whereEqualTo("uid", uid).get().await()
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        if (document.id != address.id) {
                            batch.update(dbAddress.document(document.id), "default", false)
                        }
                    }
                }
            }
            batch.set(dbAddress.document(address.id), address)
            batch.commit().await()
        } catch (e: Exception) {

        }
    }

    override suspend fun deleteAddress(address: AddressModel) {
        dbAddress.document(address.id).delete()
    }

    override suspend fun getAddresses(): List<AddressModel> {
        return try {
            val querySnapshot =
                dbAddress.whereEqualTo("uid", FirebaseAuth.getInstance().currentUser?.uid).get()
                    .await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(AddressModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAddressById(id: String): AddressModel? {
        return try {
            val querySnapshot = dbAddress.document(id).get().await()
            if (!querySnapshot.exists()) {
                null
            } else {
                querySnapshot.toObject(AddressModel::class.java)?.copy(id = id)
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getDefaultAddress(): AddressModel? {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
            val querySnapshot = dbAddress
                .whereEqualTo("uid", uid)
                .get()
                .await()

            querySnapshot.documents
                .firstOrNull { document ->
                    document.toObject(AddressModel::class.java)?.default == true
                }?.let { document ->
                    document.toObject(AddressModel::class.java)?.copy(id = document.id)
                }
        } catch (e: Exception) {
            println("Error fetching default address: ${e.message}")
            null
        }
    }
}