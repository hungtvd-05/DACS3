package com.app_computer_ecom.dack.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app_computer_ecom.dack.data.dao.SearchHistoryDao
import com.app_computer_ecom.dack.data.entity.SearchHistory

@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}