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
import com.app_computer_ecom.dack.pages.admin.ListUserPage
import com.app_computer_ecom.dack.screen.admin.AddProductScreen
import com.app_computer_ecom.dack.screen.admin.AdminScreen
import com.app_computer_ecom.dack.screen.admin.OrderDetailAdminScreen
import com.app_computer_ecom.dack.screen.admin.ReplyRatingAndCommentScreen
import com.app_computer_ecom.dack.screen.admin.UpdateProductScreen
import com.app_computer_ecom.dack.screen.employee.EmployeeAddProductScreen
import com.app_computer_ecom.dack.screen.employee.EmployeeOrderDetailScreen
import com.app_computer_ecom.dack.screen.employee.EmployeeReplyRatingAndCommentScreen
import com.app_computer_ecom.dack.screen.employee.EmployeeScreen
import com.app_computer_ecom.dack.screen.employee.EmployeeUpdateProductScreen
import com.app_computer_ecom.dack.screen.started.AuthScreen
import com.app_computer_ecom.dack.screen.started.LoginScreen
import com.app_computer_ecom.dack.screen.started.ResetPasswordScreen
import com.app_computer_ecom.dack.screen.started.SignupScreen
import com.app_computer_ecom.dack.screen.user.AccountScreen
import com.app_computer_ecom.dack.screen.user.AddAddressScreen
import com.app_computer_ecom.dack.screen.user.AddressMenuScreen
import com.app_computer_ecom.dack.screen.user.CheckRatingAndCommentScreen
import com.app_computer_ecom.dack.screen.user.CheckoutScreen
import com.app_computer_ecom.dack.screen.user.EditAccountScreen
import com.app_computer_ecom.dack.screen.user.EditAddressScreen
import com.app_computer_ecom.dack.screen.user.HomeScreen
import com.app_computer_ecom.dack.screen.user.ListProductScreen
import com.app_computer_ecom.dack.screen.user.OrderDetailUserScreen
import com.app_computer_ecom.dack.screen.user.OrderStatusScreen
import com.app_computer_ecom.dack.screen.user.OrderSuccessScreen
import com.app_computer_ecom.dack.screen.user.ProductDetailsScreen
import com.app_computer_ecom.dack.screen.user.RatingAndCommentScreen
import com.app_computer_ecom.dack.screen.user.ReviewScreen
import com.app_computer_ecom.dack.screen.user.SearchScreen
import com.app_computer_ecom.dack.screen.user.TermsScreen
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    var user = GLobalAuthViewModel.getAuthViewModel().user
    var userModel = GLobalAuthViewModel.getAuthViewModel().userModel

    var firstPage by remember { mutableStateOf("loading") }

    LaunchedEffect(userModel) {
        firstPage = if (user == null) "auth"
        else if (userModel?.role == "admin") "admin/0"
        else if (userModel?.role == "user") "home/0"
        else if (userModel?.role == "employee") "employee/0"
        else "loading"
    }

    NavHost(navController = navController, startDestination = firstPage) {
        composable("auth") {
            AuthScreen(modifier)
        }
        composable("login") {
            LoginScreen(modifier)
        }
        composable("signup") {
            SignupScreen(modifier)
        }
        composable("reset") {
            ResetPasswordScreen(modifier)
        }
        composable("home/{indexValue}") {
            var indexValue = it.arguments?.getString("indexValue")
            HomeScreen(indexValue = indexValue.toString().toInt())
        }
        composable("admin/{indexValue}") {
            var indexValue = it.arguments?.getString("indexValue")
            AdminScreen(
                indexValue = indexValue!!.toInt()
            )
        }
        composable("loading") {
            LoadingScreen()
        }
//        composable("admin/product") {
//            ProductPage(navController)
//        }
        composable("admin/addproduct") {
            AddProductScreen()
        }
        composable("admin/editproduct/{productId}") {
            var productId = it.arguments?.getString("productId").toString()
            UpdateProductScreen(navController, productId)
        }
        composable("listproduct/categoryId={categoryId}&brandId={brandId}&searchQuery={searchQuery}") {
            var categoryId = it.arguments?.getString("categoryId").toString()
            var brandId = it.arguments?.getString("brandId").toString()
            var searchQuery = it.arguments?.getString("searchQuery").toString()
            ListProductScreen(
                categoryId = categoryId,
                brandId = brandId,
                searchQuery = searchQuery,
                modifier
            )
        }
        composable("product-details/productId={productId}&lastIndexPage={lastIndexPage}") {
            var productId = it.arguments?.getString("productId").toString()
            var lastIndexPage = it.arguments?.getString("lastIndexPage").toString()
            ProductDetailsScreen(productId = productId, lastIndexPage.toInt(), modifier)
        }
        composable("search") {
            SearchScreen(modifier = modifier)
        }
        composable("checkout") {
            CheckoutScreen()
        }
        composable("menuaddress/{lastIndex}") {
            var lastIndex = it.arguments?.getString("lastIndex")!!.toInt()
            AddressMenuScreen(lastIndex)
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
            OrderDetailAdminScreen(id)
        }

        composable("orderstatus/{status}") {
            var status = it.arguments?.getString("status")!!.toInt()
            OrderStatusScreen(status)
        }
        composable("terms") {
            TermsScreen()
        }
        composable("account") {
            AccountScreen()
        }
        composable("admin/account") {
            AccountScreen(backnav = "admin/4")
        }
        composable("employee/account") {
            AccountScreen(backnav = "employee/3")
        }
        composable("admin/users") {
            ListUserPage()
        }
        composable("employee/{indexValue}") {
            var indexValue = it.arguments?.getString("indexValue")
            EmployeeScreen(
                indexValue = indexValue.toString().toInt()
            )
        }
        composable("order-details/orderId={orderId}") {
            var orderID = it.arguments?.getString("orderId").toString()
            OrderDetailUserScreen(orderID)
        }
        composable("user/rating/orderId={orderId}&selectedProduct={selectedProduct}") {
            var orderId = it.arguments?.getString("orderId").toString()
            var selectedProduct = it.arguments?.getString("selectedProduct")!!.toInt()
            RatingAndCommentScreen(orderId, selectedProduct)
        }
        composable("user/check-rating/orderId={orderId}&selectedProduct={selectedProduct}") {
            var orderId = it.arguments?.getString("orderId").toString()
            var selectedProduct = it.arguments?.getString("selectedProduct")!!.toInt()
            CheckRatingAndCommentScreen(orderId, selectedProduct)
        }
        composable("admin/reply-rating/ratingId={ratingId}") {
            var ratingId = it.arguments?.getString("ratingId").toString()
            ReplyRatingAndCommentScreen(ratingId)
        }
        composable("employee/addproduct") {
            EmployeeAddProductScreen()
        }
        composable("employee/editproduct/{productId}") {
            var productId = it.arguments?.getString("productId").toString()
            EmployeeUpdateProductScreen(navController, productId)
        }
        composable("employee/reply-rating/ratingId={ratingId}") {
            var ratingId = it.arguments?.getString("ratingId").toString()
            EmployeeReplyRatingAndCommentScreen(ratingId)
        }
        composable("employee/orderdetail/id={orderId}") {
            var id = it.arguments?.getString("orderId").toString()
            EmployeeOrderDetailScreen(id)
        }
        composable("review/{productId}") {
            val productId = it.arguments?.getString("productId").toString()
            ReviewScreen(productId)
        }
        composable("edit-account/{fieldInt}") {
            val fieldInt = it.arguments?.getString("fieldInt")?.toInt() ?: 0
            EditAccountScreen(fieldInt)
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