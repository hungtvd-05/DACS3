package com.app_computer_ecom.dack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_computer_ecom.dack.data.dao.SearchHistoryDao
import com.app_computer_ecom.dack.data.entity.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SearchViewModel(private val searchHistoryDao: SearchHistoryDao) : ViewModel() {

    val searchHistory: Flow<List<SearchHistory>> = searchHistoryDao.getAllSearchHistory()

    fun addSearchQuery(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                searchHistoryDao.insert(
                    SearchHistory(query = query, timestamp = System.currentTimeMillis())
                )
            }
        }
    }

    fun addSearchQueryWithLimit(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                searchHistoryDao.insertWithLimit(
                    SearchHistory(query = query, timestamp = System.currentTimeMillis())
                )
            }
        }
    }


    fun deleteSearch(id: Int) {
        viewModelScope.launch {
            searchHistoryDao.deleteById(id)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryDao.deleteAll()
        }
    }

    fun getSearchSuggestions(query: String): Flow<List<SearchHistory>> {
        return searchHistoryDao.getSearchSuggestions(query)
    }
}