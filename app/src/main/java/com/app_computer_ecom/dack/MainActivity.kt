package com.app_computer_ecom.dack

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.app_computer_ecom.dack.ui.theme.DACKTheme
import com.app_computer_ecom.dack.viewmodel.AuthViewModel
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel

class MainActivity : ComponentActivity() {
//    private val authViewModel: AuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GLobalAuthViewModel.initialize(application)
        setContent {
            DACKTheme {
                AppNavigation()
            }
        }
    }
}