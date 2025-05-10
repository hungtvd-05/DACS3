package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.RatingModel

interface RatingAndCommentRepository {
    suspend fun getRatingAndComment(pid: String): List<RatingModel>
    suspend fun getRatingAndCommentById(id: String): RatingModel?
    suspend fun addRatingAndComment(ratingAndCommentModel: RatingModel): String
    suspend fun updateRatingAndComment(ratingAndCommentModel: RatingModel)
}