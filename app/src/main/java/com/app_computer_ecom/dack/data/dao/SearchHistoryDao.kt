package com.app_computer_ecom.dack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app_computer_ecom.dack.data.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert
    suspend fun insert(search: SearchHistory)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    fun getAllSearchHistory(): Flow<List<SearchHistory>>

    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()

    @Query("SELECT * FROM search_history WHERE `query` LIKE :query || '%' ORDER BY timestamp DESC")
    fun getSearchSuggestions(query: String): Flow<List<SearchHistory>>
}