package com.app_computer_ecom.dack.screen.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.app_computer_ecom.dack.components.admin.HeaderViewMenu

@Composable
fun ProductScreen(navController: NavHostController) {
    BackHandler(enabled = true) {
        navController.navigate("admin/01") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
    Column {
        HeaderViewMenu(
            title = "Danh sách sản phẩm",
            onBackClick = { navController.navigate("admin/01") },
            onAddClick = {

            },
            isUploading = false
        )
    }
}