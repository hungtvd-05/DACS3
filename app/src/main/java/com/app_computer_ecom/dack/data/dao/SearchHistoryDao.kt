package com.app_computer_ecom.dack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun getHistoryCount(): Int

    @Query("DELETE FROM search_history WHERE id IN (SELECT id FROM search_history ORDER BY timestamp ASC LIMIT 1)")
    suspend fun deleteOldest()

    @Query("SELECT * FROM search_history WHERE `query` = :query")
    suspend fun getSearchHistoryByQuery(query: String): List<SearchHistory>

    @Transaction
    suspend fun insertWithLimit(search: SearchHistory, maxEntries: Int = 10) {
        val existingHistory = getSearchHistoryByQuery(search.query)
        if (existingHistory.isNullOrEmpty()) {
            insert(search)
            val count = getHistoryCount()
            if (count > maxEntries) {
                deleteOldest()
            }
        }
    }
}