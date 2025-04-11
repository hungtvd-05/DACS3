package com.app_computer_ecom.dack.screen.started

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.viewmodel.AuthViewModel

@Composable
fun ResetPasswordScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var isGoogleLoading by remember {
        mutableStateOf(false)
    }
    var context = LocalContext.current

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
            text = "You need to reset your password!!!",
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
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (isEmail(email)) {
                    isLoading = true
                    authViewModel.resetPassword(email) { success, message ->
                        if (success) {
                            AppUtil.showToast(context, "Check your email")
                            navController.navigate("login")
                        } else {
                            Toast.makeText(context, "Lỗi: $message", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else {
                    AppUtil.showToast(context, "This is not an email address!!!")
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
            Text(text = if (isLoading) "Emailing" else "Send", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "You need to login?",
                color = Color.White
            )
            Text(
                text = "Login",
                color = Color(246, 133, 1),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )
        }

    }
}
