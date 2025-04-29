package com.app_computer_ecom.dack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_computer_ecom.dack.data.dao.SearchHistoryDao
import com.app_computer_ecom.dack.data.entity.SearchHistory
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val searchHistoryDao: SearchHistoryDao) : ViewModel() {

    val searchHistory: Flow<List<SearchHistory>> = searchHistoryDao.getAllSearchHistory()
    private val _suggestions = MutableStateFlow<List<ProductModel>>(emptyList())
    val suggestions: StateFlow<List<ProductModel>> = _suggestions

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

    fun searchProducts(query: String) {
        viewModelScope.launch {
            val results = GlobalRepository.productRepository.getProductsWithFilter(
                searchQuery = query,
                categoryIds = emptyList(),
                brandIds = emptyList(),
                minPrice = 0,
                maxPrice = Int.MAX_VALUE
            )
            _suggestions.value = results
        }
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }


}