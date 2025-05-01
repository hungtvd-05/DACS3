package com.app_computer_ecom.dack.screen.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.model.AddressModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch

@Composable
fun AddressMenuScreen() {

    var addresses by remember { mutableStateOf(emptyList<AddressModel>()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        addresses = GlobalRepository.addressRepository.getAddresses()
        isLoading = false
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth().padding(top = 4.dp)
        ) {
            IconButton(
                onClick = { GlobalNavigation.navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Chọn địa chỉ nhận hàng",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {  }
                items(addresses.size) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            RadioButton(
                                selected = (addresses[it].default),
                                onClick = {
                                    isLoading = true
                                    scope.launch {
                                        GlobalRepository.addressRepository.updateAddress(addresses[it].copy(default = !addresses[it].default))
                                        GlobalNavigation.navController.popBackStack()
                                    }
                                }
                            )
                            Column(

                            ) {
                                Row {
                                    Text(
                                        text = "${addresses[it].name} | ${addresses[it].phoneNum}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "Sửa",
                                        modifier = Modifier.clickable(onClick = {
                                            GlobalNavigation.navController.navigate("editaddress/${addresses[it].id}")
                                        }),
                                        fontSize = 12.sp
                                    )
                                }
                                Row {
                                    Text(
                                        text = "${addresses[it].street}, ${addresses[it].ward}, ${addresses[it].district}, ${addresses[it].province}",
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Button(
                        onClick = {
                            GlobalNavigation.navController.navigate("addAddress")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(25, 118, 210),
                        ),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Text(
                            text = "Thêm địa chỉ"
                        )
                    }
                }
            }
        }
    }


}