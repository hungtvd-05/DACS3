package com.app_computer_ecom.dack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.app_computer_ecom.dack.data.entity.ProductHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductHistoryDao {

    @Insert
    suspend fun insert(productHistory: ProductHistory)

    @Query("SELECT * FROM product_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<ProductHistory>>

    @Query("DELETE FROM product_history WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM product_history")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM product_history")
    suspend fun getHistoryCount(): Int

    @Query("DELETE FROM product_history WHERE id IN (SELECT id FROM product_history ORDER BY timestamp ASC LIMIT 1)")
    suspend fun deleteOldest()

    @Query("SELECT COUNT(*) FROM product_history WHERE productId = :productId")
    suspend fun isProductHistoryExist(productId: String): Int

    @Transaction
    suspend fun insertIfNotExists(productHistory: ProductHistory) {
        val count = isProductHistoryExist(productHistory.productId)
        if (count == 0) {
            insert(productHistory)
        }
    }

}