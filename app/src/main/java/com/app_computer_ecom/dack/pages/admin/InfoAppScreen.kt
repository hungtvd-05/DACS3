package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.ui.theme.ThemeManager
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel

@Composable
fun InfoAppScreen() {

    val context = LocalContext.current

    Column {
        Button(
            onClick = {
                GLobalAuthViewModel.getAuthViewModel().logout()
            }
        ) {
            Text(
                text = "Logout"
            )
        }
        var isDarkModeEnabled by remember { mutableStateOf(ThemeManager.isDarkTheme) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    ThemeManager.toggleTheme(context)
                    isDarkModeEnabled = ThemeManager.isDarkTheme
                }
                .padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Chế độ nền tối", fontSize = 12.sp)
            Switch(
                checked = isDarkModeEnabled,
                onCheckedChange = {
                    ThemeManager.toggleTheme(context)
                    isDarkModeEnabled = ThemeManager.isDarkTheme
                },
                modifier = Modifier
                    .size(width = 36.dp, height = 24.dp)
                    .scale(0.8f),
            )
        }
    }
}