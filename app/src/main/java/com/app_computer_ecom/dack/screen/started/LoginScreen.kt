package com.app_computer_ecom.dack.screen.started

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var isGoogleLoading by remember {
        mutableStateOf(false)
    }
    var context = LocalContext.current
    val activityContext = context as? Activity ?: context
    var passwordVisible by remember { mutableStateOf(false) }


    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFF1A5CCC)), contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = modifier
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_teachbit),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(128.dp)
                )
                Text(
                    text = "Welcome back!",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                )

                Text(
                    text = "Sign into access your account",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(48.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email address", color = Color.White) },
                    placeholder = {
                        Text(
                            "Enter your email address",
                            color = Color(0xFFB0C4DE)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color(0xFFB0C4DE),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color(0xFFB0C4DE),
                        placeholderColor = Color(0xFFB0C4DE)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.White) },
                    placeholder = { Text("Enter your password", color = Color(0xFFB0C4DE)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color(0xFFB0C4DE),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color(0xFFB0C4DE),
                        placeholderColor = Color(0xFFB0C4DE)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (passwordVisible) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com

                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(image),
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Forgot password?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        GlobalNavigation.navController.navigate("reset") {
                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline
                )
                Spacer(modifier = Modifier.height(10.dp))


            }

            Column(
                modifier = modifier
                    .padding(horizontal = 32.dp)
                    .align(Alignment.BottomCenter)
                    .offset(0.dp, (-32).dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
                        isLoading = true
                        if (isEmail(email)) {
                            isLoading = true
                            authViewModel.login(email, password) { success, errorMessage ->
                                if (success) {
                                    if (authViewModel.userModel?.role == "user") {
                                        GlobalNavigation.navController.navigate("home") {
                                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    } else if (authViewModel.userModel?.role == "admin") {
                                        GlobalNavigation.navController.navigate("admin") {
                                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    } else {
                                        GlobalNavigation.navController.navigate("loading") {
                                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                } else {
                                    isLoading = false
                                    AppUtil.showToast(context, errorMessage ?: "Something went wrong")
                                }
                            }
                        } else {
                            isLoading = true
                            authViewModel.loginWithUsername(email, password) { success, errorMessage ->
                                if (success) {
                                    if (authViewModel.userModel?.role == "user") {
                                        GlobalNavigation.navController.navigate("home") {
                                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    } else if (authViewModel.userModel?.role == "admin") {
                                        GlobalNavigation.navController.navigate("admin") {
                                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    } else {
                                        GlobalNavigation.navController.navigate("loading") {
                                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                } else {
                                    isLoading = false
                                    AppUtil.showToast(context, errorMessage ?: "Something went wrong")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = if (isLoading) "Logging in" else "Login", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        authViewModel.loginWithGoogle(activityContext) { success, message ->
                            if (!success) {
                                Toast.makeText(
                                    context,
                                    message ?: "Lỗi đăng nhập Google",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                if (authViewModel.userModel?.role == "user") {
                                    GlobalNavigation.navController.navigate("home/0") {
                                        popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                } else {
                                    GlobalNavigation.navController.navigate("admin/0") {
                                        popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }
                    },
                    enabled = !isLoading && !isGoogleLoading,
                    border = BorderStroke(1.dp, Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp), shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isGoogleLoading) "Signing in with Google..." else "Sign in with Google",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account? ",
                        color = Color.White, fontSize = 12.sp
                    )
                    Text(
                        text = "Sign up",
                        color = Color(71, 214, 234, 255),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            GlobalNavigation.navController.navigate("signup") {
                                popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }, fontSize = 12.sp
                    )
                }
            }
        }
    }
}

fun isEmail(input: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return input.matches(emailPattern.toRegex())
}