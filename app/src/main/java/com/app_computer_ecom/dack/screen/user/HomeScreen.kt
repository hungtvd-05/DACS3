package com.app_computer_ecom.dack.screen.user

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.graphics.vector.ImageVector
import com.app_computer_ecom.dack.pages.user.CartPage
import com.app_computer_ecom.dack.pages.user.FavoritePage
import com.app_computer_ecom.dack.pages.user.HomePage
import com.app_computer_ecom.dack.pages.user.ProfilePage

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    indexValue: Int = 0
) {
    val navItems = listOf(
        NavItem("Home", Icons.Default.ShoppingCart),
        NavItem("Favorite", Icons.Default.Favorite),
        NavItem("Cart", Icons.Default.ShoppingCart),
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
//            BottomBar(
//                navItems = navItems,
//                selectedIndex = selectedIndex,
//                onItemSelected = { index ->
//                    selectedIndex = index
//                })
        }
    ) { paddingValues ->
        ContentScreen(
            modifier = modifier.padding(paddingValues),
            selectedIndex,
            onIndexSelect = { index ->
                selectedIndex = index
            })
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onIndexSelect: (Int) -> Unit
) {
    when (selectedIndex) {
        0 -> HomePage(modifier)
        1 -> FavoritePage(modifier)
        2 -> CartPage(modifier)
        3 -> ProfilePage(modifier = modifier)
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomBar(navItems: List<NavItem>, selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar {
        navItems.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = {
                    onItemSelected(index)
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
