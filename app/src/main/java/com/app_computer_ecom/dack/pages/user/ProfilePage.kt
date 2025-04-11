package com.app_computer_ecom.dack.pages.user

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@Composable
fun ProfilePage(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    Text(text = "Profile page")
    Button(
        onClick = {
            authViewModel.logout()
        }
    ) {
        Text(text = "Logout")
    }
}