package com.app_computer_ecom.dack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.app_computer_ecom.dack.ui.theme.DACKTheme
import com.app_computer_ecom.dack.ui.theme.ThemeManager
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel
import kotlinx.coroutines.launch
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK

class MainActivity : ComponentActivity() {
//    private val authViewModel: AuthViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            ThemeManager.initTheme(applicationContext)
        }
        super.onCreate(savedInstanceState)
        GLobalAuthViewModel.initialize(application)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        

        ZaloPaySDK.init(553, Environment.SANDBOX)
        setContent {
            DACKTheme {
                AppNavigation()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("ZaloPay", "onNewIntent called with intent: $intent")
        intent.let {
            ZaloPaySDK.getInstance().onResult(it)
            Log.d("ZaloPay", "Processed ZaloPay callback")
        }
    }
}