package com.app_computer_ecom.dack.screen.user

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.app_computer_ecom.dack.pages.user.CartPage
import com.app_computer_ecom.dack.pages.user.FavoritePage
import com.app_computer_ecom.dack.pages.user.HomePage
import com.app_computer_ecom.dack.pages.user.ProfilePage
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel) {
    val navItems = listOf(
        NavItem("Home", Icons.Default.ShoppingCart),
        NavItem("Favorite", Icons.Default.Favorite),
        NavItem("Cart", Icons.Default.ShoppingCart),
        NavItem("Profile", Icons.Default.Person),
    )

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                        },
                        label = {
                            Text(
                                text = navItem.label
                            )
                        }
                    )
                }

            }
        }
    ) {
        ContentScreen(authViewModel, modifier = modifier.padding(it), selectedIndex)
    }
}

@Composable
fun ContentScreen(authViewModel: AuthViewModel, modifier: Modifier = Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> HomePage(authViewModel = authViewModel, modifier)
        1 -> FavoritePage(modifier)
        2 -> CartPage(modifier)
        3 -> ProfilePage(authViewModel = authViewModel, modifier)
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector
)