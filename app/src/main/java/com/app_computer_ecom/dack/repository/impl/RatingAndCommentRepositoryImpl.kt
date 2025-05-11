package com.app_computer_ecom.dack.repository.impl

import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.CommentModel
import com.app_computer_ecom.dack.model.RatingModel
import com.app_computer_ecom.dack.repository.RatingAndCommentRepository
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RatingAndCommentRepositoryImpl: RatingAndCommentRepository {
    val db = GlobalDatabase.database
    val dbRating = db.collection("ratings")
    override suspend fun getRatingAndComment(pid: String, limit: Int): List<RatingModel> {
        return try {
            val querySnapshot = dbRating.whereEqualTo("pid", pid).get().await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(RatingModel::class.java)?.copy(id = document.id)
                }.take(limit)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAllRatingAndComment(): Flow<List<RatingModel>> = callbackFlow {
        val listenerRegistration = dbRating
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val ratings = it.documents.mapNotNull { document ->
                        document.toObject(RatingModel::class.java)?.copy(id = document.id)
                    }
                    trySend(ratings).isSuccess
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun getRatingAndCommentById(id: String): RatingModel? {
        return try {
            val querySnapshot = dbRating.document(id).get().await()
            if (!querySnapshot.exists()) {
                null
            } else {
                querySnapshot.toObject(RatingModel::class.java)?.copy(id = id)
            }
        } catch (e: Exception) {
            throw FirebaseException("Failed to get rating: ${e.message}")
        }
    }

    override suspend fun addRatingAndComment(ratingModel: RatingModel): String {
        return try {
            val documentReference = dbRating.add(ratingModel).await()
            documentReference.id
        } catch (e: Exception) {
            throw FirebaseException("Failed to add rating: ${e.message}")
        }
    }

    override suspend fun replyRatingAndComment(id: String, commentModel: CommentModel) {
        dbRating.document(id).update("commentModel", commentModel)
    }

}