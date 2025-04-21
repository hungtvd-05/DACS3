package com.app_computer_ecom.dack.viewmodel

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app_computer_ecom.dack.data.DatabaseProvider

@Composable
fun provideSearchViewModel(context: Context): SearchViewModel {
    val dao = DatabaseProvider.getDatabase(context).searchHistoryDao()
    return viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(dao) as T
        }
    })
}