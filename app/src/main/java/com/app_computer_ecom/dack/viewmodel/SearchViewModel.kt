package com.app_computer_ecom.dack.viewmodel.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_computer_ecom.dack.data.room.dao.ProductHistoryDao
import com.app_computer_ecom.dack.data.room.dao.SearchHistoryDao
import com.app_computer_ecom.dack.data.room.entity.ProductHistory
import com.app_computer_ecom.dack.data.room.entity.SearchHistory
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryDao: SearchHistoryDao,
    private val productHistoryDao: ProductHistoryDao
) : ViewModel() {

    val searchHistory: Flow<List<SearchHistory>> = searchHistoryDao.getAllSearchHistory()
    private val _suggestions = MutableStateFlow<List<ProductModel>>(emptyList())
    val suggestions: StateFlow<List<ProductModel>> = _suggestions


    val productHistory: Flow<List<ProductHistory>> = productHistoryDao.getAllHistory()


    fun addProductHistory(product: ProductModel) {
        if (product.id.isNotEmpty()) {
            viewModelScope.launch {
                productHistoryDao.insert(
                    ProductHistory(
                        productId = product.id,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

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


    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryDao.deleteAll()
        }
    }

    fun clearProductHistory() {
        viewModelScope.launch {
            productHistoryDao.deleteAll()
        }
    }


    fun searchProducts(query: String) {
        viewModelScope.launch {
            val results = GlobalRepository.productRepository.getProductsWithFilter(
                searchQuery = query,
                categoryIds = emptyList(),
                brandIds = emptyList()
            )
            _suggestions.value = results
        }
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }


}