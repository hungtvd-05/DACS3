package com.app_computer_ecom.dack

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app_computer_ecom.dack.screen.admin.AddProductScreen
import com.app_computer_ecom.dack.screen.admin.AdminScreen
import com.app_computer_ecom.dack.screen.admin.BannerScreen
import com.app_computer_ecom.dack.screen.admin.BrandScreen
import com.app_computer_ecom.dack.screen.admin.CategoryScreen
import com.app_computer_ecom.dack.screen.admin.ProductScreen
import com.app_computer_ecom.dack.screen.admin.UpdateProductScreen
import com.app_computer_ecom.dack.screen.started.AuthScreen
import com.app_computer_ecom.dack.screen.started.LoginScreen
import com.app_computer_ecom.dack.screen.started.ResetPasswordScreen
import com.app_computer_ecom.dack.screen.started.SignupScreen
import com.app_computer_ecom.dack.screen.user.HomeScreen
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    val user = authViewModel.user
    val userModel = authViewModel.userModel

    var firstPage by remember { mutableStateOf("loading") }

    LaunchedEffect(userModel) {
        firstPage = if (user == null) "auth"
        else if (userModel?.role == "admin") "admin/0"
        else if (userModel?.role == "user") "home"
        else "loading"
    }
    

    NavHost(navController = navController, startDestination = firstPage) {
        composable("auth") {
            AuthScreen(modifier, navController)
        }
        composable("login") {
            LoginScreen(modifier, navController)
        }
        composable("signup") {
            SignupScreen(modifier, navController)
        }
        composable("reset") {
            ResetPasswordScreen(modifier, navController)
        }
        composable("home") {
            HomeScreen(modifier, navController, authViewModel)
        }
        composable("admin/{indexValue}") {
            var indexValue = it.arguments?.getString("indexValue")
            AdminScreen(
                navController = navController,
                authViewModel = authViewModel,
                indexValue = indexValue.toString().toInt()
            )
        }
        composable("loading") {
            LoadingScreen()
        }
        composable("banner") {
            BannerScreen(navController)
        }
        composable("category") {
            CategoryScreen(navController)
        }
        composable("brand") {
            BrandScreen(navController)
        }
        composable("admin/product") {
            ProductScreen(navController)
        }
        composable("admin/addproduct") {
            AddProductScreen(navController)
        }
        composable("admin/editproduct/{productId}") {
            var productId = it.arguments?.getString("productId").toString()
            Log.d("productId", "productId: $productId")
            UpdateProductScreen(navController, productId)
        }
    }

}

object GlobalNavigation {
    lateinit var navController: NavHostController
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}