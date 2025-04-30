package com.app_computer_ecom.dack.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app_computer_ecom.dack.data.dao.ProductHistoryDao
import com.app_computer_ecom.dack.data.dao.SearchHistoryDao
import com.app_computer_ecom.dack.data.entity.ProductHistory
import com.app_computer_ecom.dack.data.entity.SearchHistory

@Database(
    entities = [SearchHistory::class, ProductHistory::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun productHistoryDao(): ProductHistoryDao
}