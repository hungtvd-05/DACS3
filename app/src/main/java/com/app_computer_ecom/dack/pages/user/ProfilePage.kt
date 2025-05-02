package com.app_computer_ecom.dack.pages.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.TopBar
import com.app_computer_ecom.dack.ui.theme.ThemeManager
import com.app_computer_ecom.dack.viewmodel.AuthViewModel
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel


@Composable
fun ProfilePage(
    authViewModel: AuthViewModel = GLobalAuthViewModel.getAuthViewModel(),
    modifier: Modifier = Modifier
) {

    var cartItemCount by remember { mutableStateOf(0) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar {
            GlobalNavigation.navController.navigate("home/0")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                UserProfileHeader(authViewModel = authViewModel)
            }

            item {
                OrderStatusSection()
            }

            item {
                ProfileSettingsSection()
            }


        }
    }
}


@Composable
fun UserProfileHeader(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.avatar_placeholder),
            contentDescription = "Avatar",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = authViewModel.userModel!!.name,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = authViewModel.userModel!!.email,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
        }
    }
}


@Composable
fun OrderStatusSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Đơn mua",
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Xem lịch sử mua hàng >",
                fontSize = 10.sp,
                lineHeight = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    GlobalNavigation.navController.navigate("orderstatus/3")
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    GlobalNavigation.navController.navigate("orderstatus/0")
                },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_wallet_24),
                        contentDescription = "Chờ xác nhận",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Chờ xác nhận",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 8.sp,
                        lineHeight = 10.sp
                    )
                }
            }
            IconButton(
                onClick = {
                    GlobalNavigation.navController.navigate("orderstatus/1")
                },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_package_2_24),
                        contentDescription = "Chờ lấy hàng",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Chờ lấy hàng",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 8.sp,
                        lineHeight = 10.sp
                    )
                }
            }
            IconButton(
                onClick = {
                    GlobalNavigation.navController.navigate("orderstatus/2")
                },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_local_shipping_24),
                        contentDescription = "Chờ giao hàng",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Chờ giao hàng",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 8.sp,
                        lineHeight = 10.sp
                    )
                }
            }
            IconButton(
                onClick = {
                    GlobalNavigation.navController.navigate("orderstatus/4")
                },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_cancel_24),
                        contentDescription = "Đã hủy",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Đã hủy",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 8.sp,
                        lineHeight = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSettingsSection(authViewModel: AuthViewModel = GLobalAuthViewModel.getAuthViewModel()) {

    var isDarkModeEnabled by remember { mutableStateOf(ThemeManager.isDarkTheme) }
    var showLogoutDialog by remember { mutableStateOf(false) }

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
                    .clickable { }
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
                        GlobalNavigation.navController.navigate("menuaddress")
                    }
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(text = "Địa chỉ", fontSize = 12.sp)
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
                        ThemeManager.toggleTheme()
                        isDarkModeEnabled = ThemeManager.isDarkTheme
                    }
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Chế độ nền tối", fontSize = 12.sp)
                Switch(
                    checked = isDarkModeEnabled,
                    onCheckedChange = {
                        ThemeManager.toggleTheme()
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
                    .clickable { }
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(text = "Điều khoản ứng dụng", fontSize = 12.sp)
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
                            authViewModel.logout()
                            showLogoutDialog = false
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
