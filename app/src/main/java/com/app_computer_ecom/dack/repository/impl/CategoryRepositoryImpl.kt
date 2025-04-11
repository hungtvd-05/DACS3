package com.app_computer_ecom.dack.repository.impl

import com.app_computer_ecom.dack.model.CategoryModel
import com.app_computer_ecom.dack.repository.CategoryRepository

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun getCategories(): List<CategoryModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategorybyName(): CategoryModel {
        TODO("Not yet implemented")
    }

    override suspend fun getCategorybyId(): CategoryModel {
        TODO("Not yet implemented")
    }

    override suspend fun getCategorybyIsEnable(): CategoryModel {
        TODO("Not yet implemented")
    }

    override suspend fun addCategory(category: CategoryModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(category: CategoryModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(category: CategoryModel) {
        TODO("Not yet implemented")
    }

}