package com.app_computer_ecom.dack.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.components.ProductItem
import com.app_computer_ecom.dack.data.room.entity.SearchHistory
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.viewmodel.Search.SearchViewModel
import com.app_computer_ecom.dack.viewmodel.Search.provideSearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = provideSearchViewModel(LocalContext.current),
    modifier: Modifier = Modifier
) {

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val searchHistory by viewModel.searchHistory.collectAsState(initial = emptyList())
    val suggestions by viewModel.suggestions.collectAsState(initial = emptyList())
    val productHistory by viewModel.productHistory.collectAsState(initial = emptyList())
    var isLoadingSearch by remember { mutableStateOf(false) }

    var productModels by remember { mutableStateOf<List<ProductModel>>(emptyList()) }

    LaunchedEffect(searchQuery.text) {
        if (searchQuery.text.isNotBlank()) {
            isLoadingSearch = true
            snapshotFlow { searchQuery.text }
                .debounce(500)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    viewModel.searchProducts(query)
                    isLoadingSearch = false
                }
        } else {
            viewModel.clearSuggestions()
            isLoadingSearch = false
        }
    }

    LaunchedEffect(productHistory) {
        val result = productHistory.mapNotNull {
            GlobalRepository.productRepository.getProductById(it.productId)
        }
        productModels = result
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onBack = {
                    GlobalNavigation.navController.popBackStack()
                },
                onSearch = {
                    viewModel.addSearchQueryWithLimit(searchQuery.text)
                    GlobalNavigation.navController.navigate(
                        "listproduct/categoryId=&brandId=&searchQuery=${
                            searchQuery.text.trim().lowercase()
                        }"
                    )
                },
                isLoadingSearch = isLoadingSearch

            )
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchItemList(
                        history = searchHistory,
                        suggestions = suggestions,
                        onClearSearchHistory = {
                            viewModel.clearSearchHistory()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    ProductHistoryList(productModels) {
                        viewModel.clearProductHistory()
                    }
                }


            }
        }
    }
}

@Composable
fun ProductHistoryList(productModels: List<ProductModel>, onClearProductHistory: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Sản phẩm đã xem", modifier = Modifier.padding(start = 16.dp))
        TextButton(onClick = {
            onClearProductHistory()
        }) {
            Text(text = "Xoá lịch sử xem", color = Color.Red, fontSize = 10.sp)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
//        Spacer(modifier = Modifier.height(1.dp))

        productModels.chunked(2).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { product ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        ProductItem(product, lastIndexPage = -1)
                    }
                }

                if (rowItems.size < 2) {
                    // nếu chỉ có 1 item, chèn spacer cho cột còn lại
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }

}

@Composable
fun SearchBar(
    searchQuery: TextFieldValue,
    onSearchQueryChange: (TextFieldValue) -> Unit,
    onBack: () -> Unit,
    onSearch: () -> Unit,
    isLoadingSearch: Boolean
) {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search", fontSize = 14.sp) },
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    bottomStart = 16.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    if (isLoadingSearch) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.CenterVertically),
                            strokeWidth = 2.dp
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface

                )
            )

            Button(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp
                ),
                onClick = onSearch,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


@Composable
fun SearchItemList(
    history: List<SearchHistory>,
    suggestions: List<ProductModel>,
    onClearSearchHistory: () -> Unit
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = screenHeight * 0.5f)
    ) {
        if (history.isNotEmpty() && suggestions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        onClearSearchHistory()
                    }) {
                Text(
                    text = "Xoá lịch sử tìm kiếm",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = 16.dp),
                    color = Color.Gray
                )
                Divider(
                    thickness = 1.dp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            items(suggestions) { item ->
                SearchItem(text = item.name, isHistory = false)
            }

            items(history) { item ->
                SearchItem(text = item.query)
            }

        }

    }
}

@Composable
fun SearchItem(
    text: String,
    isHistory: Boolean = true,
    viewModel: SearchViewModel = provideSearchViewModel(LocalContext.current)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(46.dp)
            .clickable {
                viewModel.addSearchQueryWithLimit(text)

                GlobalNavigation.navController.navigate(
                    "listproduct/categoryId=&brandId=&searchQuery=${text}"
                )
            }
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, end = 40.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        )

        if (isHistory) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                tint = Color.DarkGray
            )
        }
    }
}