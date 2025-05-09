package com.app_computer_ecom.dack.screen.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app_computer_ecom.dack.pages.admin.Dashboard
import com.app_computer_ecom.dack.pages.admin.InfoAppScreen
import com.app_computer_ecom.dack.pages.admin.Menu
import com.app_computer_ecom.dack.pages.admin.OrderPage
import com.app_computer_ecom.dack.screen.user.NavItem

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AdminScreen(modifier: Modifier = Modifier, indexValue: Int = 0) {
    val navItems = listOf(
        NavItem("Dashboard", Icons.Default.Home),
        NavItem("Menu", Icons.Default.Menu),
        NavItem("Đơn hàng", Icons.Default.DateRange),
        NavItem("Thông tin", Icons.Default.Info),
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
        ContentAdminScreen(modifier = modifier.padding(paddingValues), selectedIndex)
    }
}

@Composable
fun ContentAdminScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> Dashboard(modifier)
        1 -> Menu(modifier)
        2 -> OrderPage(modifier)
        3 -> InfoAppScreen()
    }
}
