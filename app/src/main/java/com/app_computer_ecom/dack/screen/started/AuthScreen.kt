package com.app_computer_ecom.dack.screen.started

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthScreen(modifier: Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Image(
//            painter = painterResource(id = R.drawable._175544_firebase_google_icon),
//            contentDescription = "Logo",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//        )
        Text(
            text = "KotlinFire",
            style = TextStyle(
                fontSize = 36.sp,
                color = Color(255, 202, 40),
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = "CRUD",
            style = TextStyle(
                fontSize = 36.sp,
                color = Color(211, 143, 61),
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(90.dp))
        Button(
            onClick = {
                navController.navigate("login")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(246, 133, 1),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(text = "Login", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedButton(
            onClick = {
                navController.navigate("signup")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(text = "Sign Up", fontSize = 22.sp, color = Color(246, 133, 1))
        }
    }
}