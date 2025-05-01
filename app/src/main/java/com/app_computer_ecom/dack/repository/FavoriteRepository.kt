package com.app_computer_ecom.dack.repository

import android.content.Context
import com.app_computer_ecom.dack.model.FavoriteModel


interface FavoriteRepository {
    suspend fun getFavoritesByUid(): List<FavoriteModel>
    suspend fun isFavorite(pid: String): Boolean
    suspend fun addToFavorite(context: Context, productId: String)
    suspend fun deleteFavorite(context: Context, productId: String)
}