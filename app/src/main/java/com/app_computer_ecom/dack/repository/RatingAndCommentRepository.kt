package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.CommentModel
import com.app_computer_ecom.dack.model.RatingModel
import kotlinx.coroutines.flow.Flow

interface RatingAndCommentRepository {
    suspend fun getRatingAndComment(pid: String, limit: Int = Int.MAX_VALUE): List<RatingModel>
    suspend fun getAllRatingAndComment(): Flow<List<RatingModel>>
    suspend fun getRatingAndCommentById(id: String): RatingModel?
    suspend fun addRatingAndComment(ratingAndCommentModel: RatingModel): String
    suspend fun replyRatingAndComment(id: String, commentModel: CommentModel)
}