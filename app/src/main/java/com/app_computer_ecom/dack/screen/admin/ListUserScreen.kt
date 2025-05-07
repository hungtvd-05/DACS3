package com.app_computer_ecom.dack.screen.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.model.UserModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch

@Composable
fun ListUserScreen() {
    BackHandler(enabled = true) {
        GlobalNavigation.navController.navigate("admin/1") {
            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    var isLoading by remember { mutableStateOf(true) }
    var users by remember { mutableStateOf<List<UserModel>?>(null) }
    var listRole = listOf("user", "employee", "admin")

    LaunchedEffect(Unit) {
        users = GlobalRepository.userRepository.getUsers()
        isLoading = false
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { GlobalNavigation.navController.navigate("admin/1") },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Danh sách tài khoản",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                users?.forEach {
                    item {
                        UserItem(user = it, listRole = listRole)
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    user: UserModel,
    listRole: List<String>
) {
    var selected by remember { mutableStateOf(user.role) }
    var lastSelected by remember { mutableStateOf(user.role) }
    val scope = rememberCoroutineScope()
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Tên tài khoản: ${user.username}\nEmail: ${user.email}\nRole: ${lastSelected}"
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                RoleDropDownFun(
                    statusList = listRole,
                    selected = selected,
                    onStatusSelected = {
                        selected = it
                    }
                )
                Button(
                    onClick = {
                        if (selected != lastSelected) {
                            lastSelected = selected
                            scope.launch {
                                GlobalRepository.userRepository.updateUserRole(user, lastSelected)
                            }
                        }
                    }
                ) {
                    Text(text = "Cập nhật")
                }
            }
        }
    }
}

@Composable
fun RoleDropDownFun(
    statusList: List<String>,
    selected: String,
    onStatusSelected: (String) -> Unit,
) {
    var dropControl by remember { mutableStateOf(false) }
    var selectedRole = selected
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                if (statusList.isNotEmpty()) {
                    dropControl = true
                }
            },
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp)
                    .padding(5.dp)
            ) {
                Text(text = selectedRole)
                Icon(
                    imageVector = if (dropControl) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = ""
                )
            }
            DropdownMenu(
                expanded = dropControl,
                onDismissRequest = { dropControl = false },
                containerColor = Color.White
            ) {
                statusList.forEachIndexed { index, role ->
                    DropdownMenuItem(
                        text = { Text(role) },
                        onClick = {
                            onStatusSelected(role)
                            dropControl = false
                        },
                    )
                }
            }
        }
    }
}