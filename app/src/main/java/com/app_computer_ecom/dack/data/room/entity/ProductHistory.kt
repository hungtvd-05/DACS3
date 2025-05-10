package com.app_computer_ecom.dack.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_history")
data class ProductHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val timestamp: Long
)
