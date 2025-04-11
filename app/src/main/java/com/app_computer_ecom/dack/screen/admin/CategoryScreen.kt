package com.app_computer_ecom.dack.screen.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.app_computer_ecom.dack.components.admin.HeaderViewMenu

@Composable
fun CategoryScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        HeaderViewMenu(
            title = "Category",
            onBackClick = { navController.navigate("admin/1") },
            onAddClick = { navController.navigate("addCategory") },
            isUploading = false
        )
    }
}