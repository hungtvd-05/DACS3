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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.model.Status
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.ui.theme.ThemeManager
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel


@Composable
fun ProfilePage(
    modifier: Modifier = Modifier
) {

    var cartItemCount by remember { mutableIntStateOf(0) }



    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
//        TopBar {
//            GlobalNavigation.navController.navigate("home/0")
//        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                UserProfileHeader()
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
    modifier: Modifier = Modifier
) {

    val avatarUrl by remember { mutableStateOf(GLobalAuthViewModel.getAuthViewModel().userModel?.avatar) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
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
                fontSize = 12.sp
            )
        }
    }
}


@Composable
fun OrderStatusSection(
    modifier: Modifier = Modifier
) {

    val statusList = listOf(
        Status.create(
            id = 0,
            title = "Chờ xác nhận",
            idPainterResource = R.drawable.outline_wallet_24
        ),
        Status.create(
            id = 1,
            title = "Chờ lấy hàng",
            idPainterResource = R.drawable.outline_package_2_24
        ),
        Status.create(
            id = 2,
            title = "Chờ giao hàng",
            idPainterResource = R.drawable.outline_local_shipping_24
        ),
        Status.create(
            id = 4,
            title = "Đã huỷ",
            idPainterResource = R.drawable.outline_cancel_24
        ),
    )

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


            statusList.forEach {
                Column(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                ) {
                    OrderStatusItem(it)
                }

            }

        }
    }
}


@Composable
fun OrderStatusItem(status: Status) {

    var orderModels by remember { mutableStateOf(emptyList<OrderModel>()) }
    var quantity by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {

        orderModels = GlobalRepository.orderRepository.getOrdersByUidAndStatus(
            uid = GLobalAuthViewModel.getAuthViewModel().userModel!!.uid,
            status = status.id
        )
        quantity = orderModels.size
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {

        IconButton(
            onClick = {
                GlobalNavigation.navController.navigate("orderstatus/${status.id}")
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(status.idPainterResource),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = status.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 8.sp,
                    lineHeight = 10.sp
                )
            }
        }

        if (quantity > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(12.dp, 2.dp)
                    .size(16.dp)
                    .background(Color(230, 81, 0), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (quantity < 100) quantity.toString() else "99+",
                    color = Color.White,
                    fontSize = 6.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 8.sp
                )
            }
        }
    }
}

@Composable
fun ProfileSettingsSection() {

    var isDarkModeEnabled by remember { mutableStateOf(ThemeManager.isDarkTheme) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
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
                        GlobalNavigation.navController.navigate("account") {
                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
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
                        GlobalNavigation.navController.navigate("menuaddress/1") {
                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
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
                    .clickable {
                        GlobalNavigation.navController.navigate("terms")
                    }
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
                Text(text = "Đăng xuất", fontSize = 12.sp, color = Color(230, 81, 0))
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
