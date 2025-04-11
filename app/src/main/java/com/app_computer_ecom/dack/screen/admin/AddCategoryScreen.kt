package com.app_computer_ecom.dack.screen.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.app_computer_ecom.dack.components.admin.HeaderViewMenu

@Composable
fun AddCategoryScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HeaderViewMenu(
            title = "Add Category",
            onBackClick = { navController.popBackStack() },
            onAddClick = { navController.navigate("addCategory") },
            isUploading = false
        )
    }
}