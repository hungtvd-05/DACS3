package com.app_computer_ecom.dack.viewmodel.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app_computer_ecom.dack.data.room.dao.ProductHistoryDao
import com.app_computer_ecom.dack.data.room.dao.SearchHistoryDao

class SearchViewModelFactory(
    private val searchHistoryDao: SearchHistoryDao,
    private val productHistoryDao: ProductHistoryDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(searchHistoryDao, productHistoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}