package com.app_computer_ecom.dack.screen.user

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.app_computer_ecom.dack.pages.user.CartPage
import com.app_computer_ecom.dack.pages.user.FavoritePage
import com.app_computer_ecom.dack.pages.user.HomePage
import com.app_computer_ecom.dack.pages.user.ProfilePage
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
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
            BottomBar(
                navItems = navItems,
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                })
        },
//        topBar = {
//            if (selectedIndex != 4) {
//                TopBar(
//                    onSearchClick = { selectedIndex = 4 },
//                    onCartClick = { selectedIndex = 2 }
//                )
//            }
//        }
    ) {
        ContentScreen(
            authViewModel,
            modifier = modifier.padding(it),
            selectedIndex,
            onIndexSelect = { index ->
                selectedIndex = index
            })
    }
}

@Composable
fun ContentScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onIndexSelect: (Int) -> Unit
) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onSearchClick: () -> Unit, onCartClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { onSearchClick() }
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Search",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onCartClick) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Cart"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}


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
