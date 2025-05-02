package com.app_computer_ecom.dack

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import com.app_computer_ecom.dack.screen.admin.OrderDetailScreen
import com.app_computer_ecom.dack.screen.admin.ProductScreen
import com.app_computer_ecom.dack.screen.admin.UpdateProductScreen
import com.app_computer_ecom.dack.screen.started.AuthScreen
import com.app_computer_ecom.dack.screen.started.LoginScreen
import com.app_computer_ecom.dack.screen.started.ResetPasswordScreen
import com.app_computer_ecom.dack.screen.started.SignupScreen
import com.app_computer_ecom.dack.screen.user.AddAddressScreen
import com.app_computer_ecom.dack.screen.user.AddressMenuScreen
import com.app_computer_ecom.dack.screen.user.CheckoutScreen
import com.app_computer_ecom.dack.screen.user.EditAddressScreen
import com.app_computer_ecom.dack.screen.user.HomeScreen
import com.app_computer_ecom.dack.screen.user.ListProductScreen
import com.app_computer_ecom.dack.screen.user.OrderStatusScreen
import com.app_computer_ecom.dack.screen.user.OrderSuccessScreen
import com.app_computer_ecom.dack.screen.user.ProductDetailsScreen
import com.app_computer_ecom.dack.screen.user.SearchScreen
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    val user = GLobalAuthViewModel.getAuthViewModel().user
    val userModel = GLobalAuthViewModel.getAuthViewModel().userModel

    var firstPage by remember { mutableStateOf("loading") }

    LaunchedEffect(userModel) {
        firstPage = if (user == null) "auth"
        else if (userModel?.role == "admin") "admin/0"
        else if (userModel?.role == "user") "home/0"
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
        composable("home/{indexValue}") {
            var indexValue = it.arguments?.getString("indexValue")
            HomeScreen(indexValue = indexValue.toString().toInt())
        }
        composable("admin/{indexValue}") {
            var indexValue = it.arguments?.getString("indexValue")
            AdminScreen(
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
            UpdateProductScreen(navController, productId)
        }
        composable("listproduct/categoryId={categoryId}&brandId={brandId}&searchQuery={searchQuery}") {
            var categoryId = it.arguments?.getString("categoryId").toString()
            var brandId = it.arguments?.getString("brandId").toString()
            var searchQuery = it.arguments?.getString("searchQuery").toString()
            ListProductScreen(categoryId = categoryId, brandId = brandId, searchQuery = searchQuery)
        }
        composable("product-details/productId={productId}") {
            var productId = it.arguments?.getString("productId").toString()
            ProductDetailsScreen(productId = productId)
        }
        composable("search") {
            SearchScreen()
        }
        composable("checkout") {
            CheckoutScreen()
        }
        composable("menuaddress") {
            AddressMenuScreen()
        }
        composable("addaddress") {
            AddAddressScreen()
        }
        composable("editaddress/{addressId}") {
            var addressId = it.arguments?.getString("addressId").toString()
            EditAddressScreen(addressId = addressId)
        }
        composable("order-success") {
            OrderSuccessScreen()
        }
        composable("admin/orderdetail/id={orderId}") {
            var id = it.arguments?.getString("orderId").toString()
            OrderDetailScreen(id)
        }

        composable("orderstatus/{status}") {
            var status = it.arguments?.getString("status")!!.toInt()
            OrderStatusScreen(status)
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