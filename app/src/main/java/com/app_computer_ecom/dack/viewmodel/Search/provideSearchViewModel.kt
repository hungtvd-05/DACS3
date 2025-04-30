package com.app_computer_ecom.dack.viewmodel.Search

import DatabaseProvider
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun provideSearchViewModel(context: Context): SearchViewModel {
    val searchHistoryDao = DatabaseProvider.getDatabase(context).searchHistoryDao()
    val productHistoryDao = DatabaseProvider.getDatabase(context).productHistoryDao()

    return viewModel(factory = SearchViewModelFactory(searchHistoryDao, productHistoryDao))
}