package com.app_computer_ecom.dack.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.repository.GlobalRepository

@Composable
fun TopBar(title: String = "", isShowCard: Boolean = true, onBack: () -> Unit) {

    BackHandler {
        onBack()
    }

    var cartItemCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        if (isShowCard) {
            cartItemCount = GlobalRepository.cartRepository.getCartList().first.size
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            onBack()
        }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.padding(end = 4.dp)
        ) {
            if (isShowCard) {
                IconButton(
                    onClick = {
                        GlobalNavigation.navController.navigate("home/2")
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart",
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(16.dp)
                                .background(Color.Red, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (cartItemCount < 100) cartItemCount.toString() else "99+",
                                color = Color.White,
                                fontSize = 6.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 8.sp
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = {
                    GlobalNavigation.navController.navigate("home/0")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}