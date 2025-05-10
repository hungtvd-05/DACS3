package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.ui.theme.ThemeManager
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel

@Composable
fun InfoAppScreen() {

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        item {
            UserProfileHeader()
            ProfileSettingsSection()
        }

    }
}

@Composable
fun UserProfileHeader(
    modifier: Modifier = Modifier
) {

    val avatarUrl by remember { mutableStateOf(GLobalAuthViewModel.getAuthViewModel().userModel?.avatar) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {

        if (avatarUrl != null) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.avatar_placeholder),
                contentDescription = "Avatar",
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = GLobalAuthViewModel.getAuthViewModel().userModel?.name.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = GLobalAuthViewModel.getAuthViewModel().userModel?.email.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Light,
                fontSize = 10.sp,
                lineHeight = 12.sp
            )

            Text(
                text = GLobalAuthViewModel.getAuthViewModel().userModel?.role
                    ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    ?: "",
                color = Color(239, 124, 124, 255),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun ProfileSettingsSection() {

    var isDarkModeEnabled by remember { mutableStateOf(ThemeManager.isDarkTheme) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                    }
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(text = "Tài khoản & bảo mật", fontSize = 12.sp)
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                color = MaterialTheme.colorScheme.background
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
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

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                color = MaterialTheme.colorScheme.background
            )


        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(text = "Giới thiệu", fontSize = 12.sp)
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                color = MaterialTheme.colorScheme.background
            )


        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showLogoutDialog = true
                    }
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(text = "Đăng xuất", fontSize = 12.sp, color = Color.Red)
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                color = MaterialTheme.colorScheme.background
            )


        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Xác nhận đăng xuất") },
                text = { Text("Bạn có chắc muốn đăng xuất khỏi tài khoản?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            GLobalAuthViewModel.getAuthViewModel().logout()

                        }
                    ) {
                        Text("Xác nhận", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text("Hủy", color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                textContentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}