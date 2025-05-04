package com.app_computer_ecom.dack.screen.user

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.TopBar
import com.app_computer_ecom.dack.model.UserModel
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel

@Composable
fun AccountScreen() {

    var userModel = GLobalAuthViewModel.getAuthViewModel().userModel ?: UserModel()
    var showChangePassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar(isShowCard = false) {
            GlobalNavigation.navController.navigate("home/3")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.avatar_placeholder),
                            contentDescription = "",
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(30.dp)
                                .background(Color(0, 0, 0, 100), CircleShape)
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tên người dùng",
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = userModel.username,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Họ và tên",
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = userModel.name,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mail",
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = userModel.email,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                        .clickable {
                            showChangePassword = !showChangePassword
                        }
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Đổi mật khẩu", fontSize = 12.sp, lineHeight = 14.sp)
                    Icon(
                        imageVector = if (showChangePassword) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (showChangePassword) {
                    ChangePasswordSection()
                }
            }


        }
    }
}

@Composable
fun ChangePasswordSection() {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Mật khẩu hiện tại", fontSize = 12.sp) },
            visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                IconButton(
                    onClick = { currentPasswordVisible = !currentPasswordVisible },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        painter = painterResource(if (currentPasswordVisible) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                        contentDescription = if (currentPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Mật khẩu mới", fontSize = 12.sp) },
            visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                IconButton(
                    onClick = { newPasswordVisible = !newPasswordVisible },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        painter = painterResource(if (newPasswordVisible) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                        contentDescription = if (newPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Xác nhận mật khẩu", fontSize = 12.sp) },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    println("Submit on Done: $currentPassword -> $newPassword")
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = { confirmPasswordVisible = !confirmPasswordVisible },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        painter = painterResource(if (confirmPasswordVisible) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                        contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                println("Xác nhận đổi mật khẩu")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text("Xác nhận đổi mật khẩu", fontSize = 14.sp)
        }
    }
}
