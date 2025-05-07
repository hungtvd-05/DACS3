package com.app_computer_ecom.dack.screen.employee

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app_computer_ecom.dack.pages.admin.InfoAppScreen
import com.app_computer_ecom.dack.pages.employee.Menu
import com.app_computer_ecom.dack.pages.admin.OrderPage
import com.app_computer_ecom.dack.screen.user.NavItem

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun EmployeeScreen(modifier: Modifier = Modifier, indexValue: Int = 0) {
    val navItems = listOf(
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
        ContentEmployeeScreen(modifier = modifier.padding(paddingValues), selectedIndex)
    }
}

@Composable
fun ContentEmployeeScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> Menu(modifier)
        1 -> OrderPage(modifier)
        2 -> InfoAppScreen()
    }
}
