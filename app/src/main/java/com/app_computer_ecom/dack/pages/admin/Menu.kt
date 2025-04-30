package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app_computer_ecom.dack.components.admin.HeaderViewAdmin
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@Composable
fun Menu(modifier: Modifier = Modifier, authViewModel: AuthViewModel, navController: NavHostController) {
    val itemMenus = listOf(
        NavItem("Product", Icons.Default.DateRange),
        NavItem("Category", Icons.Default.Menu),
        NavItem("Brand", Icons.Default.LocationOn),
        NavItem("Banner", Icons.Default.Person),
        NavItem("Policy", Icons.Default.Person),
        NavItem("Review", Icons.Default.Person),
        NavItem("user", Icons.Default.Person),
    )

    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderViewAdmin(authViewModel, modifier)
//        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp)
        ) {
            items(itemMenus.size) { index ->
                ItemMenu(itemMenus[index], navController, index)
            }
        }
    }
}

@Composable
fun ItemMenu(navItem: NavItem, navController: NavHostController, index: Int) {
    Card(
        modifier = Modifier.size(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = {
            when (index) {
                0 -> navController.navigate("admin/product")
                1 -> navController.navigate("category")
                2 -> navController.navigate("brand")
                3 -> navController.navigate("banner")
                4 -> navController.navigate("policy")
                5 -> navController.navigate("review")
                6 -> navController.navigate("user")
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(imageVector = navItem.icon, contentDescription = navItem.label, modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = navItem.label, textAlign = TextAlign.Center)
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector
)