package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.CategoryModel

interface CategoryRepository {
    suspend fun getCategories(): List<CategoryModel>
    suspend fun getCategorybyId(categoryId: String): CategoryModel?
    suspend fun getCategorybyIsEnable(): List<CategoryModel>
    suspend fun addCategory(category: CategoryModel)
    suspend fun updateCategory(category: CategoryModel)
    suspend fun deleteCategory(category: CategoryModel)
}