package com.app_computer_ecom.dack.pages.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app_computer_ecom.dack.components.BannerView
import com.app_computer_ecom.dack.components.HeaderView
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@Composable
fun HomePage(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderView(authViewModel, modifier)
        Spacer(modifier = Modifier.height(10.dp))
        BannerView(modifier = Modifier)
    }
}