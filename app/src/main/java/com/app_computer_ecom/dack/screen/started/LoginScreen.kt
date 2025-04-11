package com.app_computer_ecom.dack.screen.started

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel = viewModel()) {
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(48, 63, 82))
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "KotlinFire",
            style = TextStyle(
                fontSize = 24.sp,
                color = Color(255, 202, 40),
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = "CRUD",
            style = TextStyle(
                fontSize = 24.sp,
                color = Color(211, 143, 61),
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Welcome back!",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Sign in to your account",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(60.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email address", color = Color(211, 143, 61)) },
            placeholder = { Text("Enter your email address", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(211, 143, 61),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White
            ),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)

        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color(211, 143, 61)) },
            placeholder = { Text("Enter your password", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(211, 143, 61),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White
            ),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Forgot password?",
            color = Color(246, 133, 1),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate("reset")
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                isLoading = true
                if (isEmail(email)) {
                    isLoading = true
                    authViewModel.login(email, password) { success, errorMessage ->
                        if (success) {
                            if (authViewModel.userModel?.role == "user") {
                                navController.navigate("home")
                            } else if (authViewModel.userModel?.role == "admin") {
                                navController.navigate("admin")
                            } else {
                                navController.navigate("loading")
                            }
                        } else {
                            isLoading = false
                            AppUtil.showToast(context, errorMessage ?: "Something went wrong")
                        }
                    }
                }
                else {
                    isLoading = true
                    authViewModel.loginWithUsername(email, password) { success, errorMessage ->
                        if (success) {
                            if (authViewModel.userModel?.role == "user") {
                                navController.navigate("home")
                            } else if (authViewModel.userModel?.role == "admin") {
                                navController.navigate("admin")
                            } else {
                                navController.navigate("loading")
                            }
                        } else {
                            isLoading = false
                            AppUtil.showToast(context, errorMessage ?: "Something went wrong")
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(246, 133, 1),
                contentColor = Color.White
            ),
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(text = if (isLoading) "Logging in" else "Login", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "OR",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = {
                authViewModel.loginWithGoogle(activityContext) { success, message ->
                    if (!success) {
                        Toast.makeText(context, message ?: "Lỗi đăng nhập Google", Toast.LENGTH_SHORT).show()
                    } else {
                        if (authViewModel.userModel?.role == "user") {
                            navController.navigate("home")
                        } else {
                            navController.navigate("admin")
                        }
                    }
                }
            },
            enabled = !isLoading && !isGoogleLoading,
            border = BorderStroke(1.dp, Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
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
                    fontSize = 18.sp
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
                color = Color.White
            )
            Text(
                text = "Sign up",
                color = Color(246, 133, 1),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("signup")
                }
            )
        }

    }
}

fun isEmail(input: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return input.matches(emailPattern.toRegex())
}