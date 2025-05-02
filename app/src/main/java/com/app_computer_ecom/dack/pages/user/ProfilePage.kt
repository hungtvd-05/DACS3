package com.app_computer_ecom.dack.pages.user

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app_computer_ecom.dack.viewmodel.AuthViewModel
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    Text(text = "Profile page")
    Button(
        onClick = {
            GLobalAuthViewModel.getAuthViewModel().logout()
        }
    ) {
        Text(text = "Logout")
    }
}