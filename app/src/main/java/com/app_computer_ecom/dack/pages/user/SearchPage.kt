package com.app_computer_ecom.dack.pages.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.data.entity.SearchHistory
import com.app_computer_ecom.dack.viewmodel.SearchViewModel
import com.app_computer_ecom.dack.viewmodel.provideSearchViewModel

@Composable
fun SearchPage(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SearchViewModel = provideSearchViewModel(LocalContext.current)
) {

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val searchHistory by viewModel.searchHistory.collectAsState(initial = emptyList())

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onBack = onBack,
            onSearch = {
                viewModel.addSearchQuery(searchQuery.text)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchItemList(searchHistory)
    }
}

@Composable
fun SearchBar(
    searchQuery: TextFieldValue,
    onSearchQueryChange: (TextFieldValue) -> Unit,
    onBack: () -> Unit,
    onSearch: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .padding(horizontal = 8.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.fillMaxHeight()
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        )

        Button(
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomStart = 0.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp
            ),
            onClick = {
                onSearch()
            },
            modifier = Modifier.fillMaxHeight()
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    }
}

@Composable
fun SearchItemList(
    searchHistory: List<SearchHistory>
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(searchHistory) { item ->
            SearchItem(text = item.query)
        }
    }
}

@Composable
fun SearchItem(text: String, isHistory: Boolean = true) {
    Box(

        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clickable { }) {
        Text(
            text = text, fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp),
        )
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.BottomCenter)
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

@Preview(showBackground = true)
@Composable
fun SearchPagePreview() {
    SearchPage(modifier = Modifier, onBack = {})
}
