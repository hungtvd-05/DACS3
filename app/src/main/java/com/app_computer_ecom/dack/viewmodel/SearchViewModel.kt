package com.app_computer_ecom.dack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_computer_ecom.dack.data.dao.SearchHistoryDao
import com.app_computer_ecom.dack.data.entity.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SearchViewModel(private val searchHistoryDao: SearchHistoryDao) : ViewModel() {

    // Lấy danh sách lịch sử tìm kiếm
    val searchHistory: Flow<List<SearchHistory>> = searchHistoryDao.getAllSearchHistory()

    // Thêm một từ khóa tìm kiếm mới
    fun addSearchQuery(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                searchHistoryDao.insert(
                    SearchHistory(query = query, timestamp = System.currentTimeMillis())
                )
            }
        }
    }

    // Xóa một mục lịch sử
    fun deleteSearch(id: Int) {
        viewModelScope.launch {
            searchHistoryDao.deleteById(id)
        }
    }

    // Xóa toàn bộ lịch sử
    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryDao.deleteAll()
        }
    }

    // Lấy gợi ý tìm kiếm
    fun getSearchSuggestions(query: String): Flow<List<SearchHistory>> {
        return searchHistoryDao.getSearchSuggestions(query)
    }
}