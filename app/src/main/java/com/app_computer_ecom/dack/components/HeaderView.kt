package com.app_computer_ecom.dack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@Composable
fun HeaderView(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    var name by remember {
        mutableStateOf(authViewModel.userModel?.username)
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Welcome back")
            Text(text = name.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
    }
}