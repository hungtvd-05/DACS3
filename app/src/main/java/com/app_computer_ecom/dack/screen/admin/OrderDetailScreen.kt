package com.app_computer_ecom.dack.screen.admin

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.app_computer_ecom.dack.GlobalNavigation

@Composable
fun OrderDetailScreen() {
    BackHandler(enabled = true) {
        GlobalNavigation.navController.navigate("admin/2") {
            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}