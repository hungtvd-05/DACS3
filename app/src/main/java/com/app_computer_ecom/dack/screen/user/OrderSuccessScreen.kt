package com.app_computer_ecom.dack.screen.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app_computer_ecom.dack.GlobalNavigation

@Composable
fun OrderSuccessScreen() {
    BackHandler(enabled = true) {
        GlobalNavigation.navController.navigate("home/0") {
            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color(46, 125, 50),
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Bạn đã đặt hàng thành công"
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                GlobalNavigation.navController.navigate("home/0")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(25, 118, 210),
            )
        ) {
            Text(
                text = "Quay về trang chủ"
            )
        }
    }
}