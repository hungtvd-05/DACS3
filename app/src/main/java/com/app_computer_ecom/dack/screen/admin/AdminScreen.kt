package com.app_computer_ecom.dack.screen.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.app_computer_ecom.dack.pages.admin.Dashboard
import com.app_computer_ecom.dack.pages.admin.Menu
import com.app_computer_ecom.dack.pages.user.ProfilePage
import com.app_computer_ecom.dack.screen.user.NavItem
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AdminScreen(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel = viewModel(), indexValue: Int = 0) {
    val navItems = listOf(
        NavItem("Dashboard", Icons.Default.DateRange),
        NavItem("Menu", Icons.Default.Menu),
        NavItem("Profile", Icons.Default.Person),
    )

    var selectedIndex by remember {
        mutableStateOf(indexValue)
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
    ) { paddingValues ->
        ContentAdminScreen(authViewModel, modifier = modifier.padding(paddingValues), selectedIndex, navController)
    }
}

@Composable
fun ContentAdminScreen(authViewModel: AuthViewModel, modifier: Modifier = Modifier, selectedIndex: Int, navController: NavHostController) {
    when (selectedIndex) {
        0 -> Dashboard(modifier)
        1 -> Menu(modifier, authViewModel, navController)
        2 -> ProfilePage(authViewModel = authViewModel, modifier)
    }
}
