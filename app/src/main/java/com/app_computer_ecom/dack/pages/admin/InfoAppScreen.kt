package com.app_computer_ecom.dack.pages.admin

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel

@Composable
fun InfoAppScreen() {
    Button(
        onClick = {
            GLobalAuthViewModel.getAuthViewModel().logout()
        }
    ) {
        Text(
            text = "Logout"
        )
    }
}