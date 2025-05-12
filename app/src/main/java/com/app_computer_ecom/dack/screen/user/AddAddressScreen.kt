package com.app_computer_ecom.dack.screen.user

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.model.AddressModel
import com.app_computer_ecom.dack.model.District
import com.app_computer_ecom.dack.model.Province
import com.app_computer_ecom.dack.model.Ward
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen() {
    var name by remember { mutableStateOf("") }
    var phoneNum by remember { mutableStateOf("") }
    val context = LocalContext.current
    val provinces = loadProvincesFromAssets(context)
    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }
    var selectedWard by remember { mutableStateOf<Ward?>(null) }
    var street by remember { mutableStateOf("") }
    var expandedProvince by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedWard by remember { mutableStateOf(false) }
    var defaultAddress by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
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
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Thêm địa chỉ",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item { }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Địa chỉ",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                label = {
                                    Text(
                                        text = "Họ và tên",
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                            TextField(
                                value = phoneNum,
                                onValueChange = { phoneNum = it },
                                label = {
                                    Text(
                                        text = "Số điện thoại",
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                            ExposedDropdownMenuBox(
                                expanded = expandedProvince,
                                onExpandedChange = { expandedProvince = !expandedProvince }
                            ) {
                                TextField(
                                    value = selectedProvince?.Name ?: "Chọn tỉnh/thành phố",
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedContainerColor = MaterialTheme.colorScheme.surface
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedProvince,
                                    onDismissRequest = { expandedProvince = false },
                                    containerColor = Color.White
                                ) {
                                    provinces.forEach { province ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(text = province.Name)
                                            },
                                            onClick = {
                                                selectedProvince = province
                                                selectedDistrict = null
                                                selectedWard = null
                                                expandedProvince = false
                                            }
                                        )
                                    }
                                }
                            }
                            ExposedDropdownMenuBox(
                                expanded = expandedDistrict,
                                onExpandedChange = { expandedDistrict = !expandedDistrict }
                            ) {
                                TextField(
                                    value = selectedDistrict?.Name ?: "Chọn quận/huyện",
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = selectedProvince != null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedDistrict,
                                    onDismissRequest = { expandedDistrict = false },
                                    containerColor = Color.White
                                ) {
                                    selectedProvince?.Districts?.forEach { district ->
                                        androidx.compose.material.DropdownMenuItem(
                                            onClick = {
                                                selectedDistrict = district
                                                selectedWard = null
                                                expandedDistrict = false
                                            }
                                        ) {
                                            Text(district.Name)
                                        }
                                    }
                                }
                            }
                            ExposedDropdownMenuBox(
                                expanded = expandedWard,
                                onExpandedChange = { expandedWard = !expandedWard }
                            ) {
                                TextField(
                                    value = selectedWard?.Name ?: "Chọn phường/xã",
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = selectedDistrict != null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedContainerColor = MaterialTheme.colorScheme.surface
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedWard,
                                    onDismissRequest = { expandedWard = false },
                                    containerColor = Color.White
                                ) {
                                    selectedDistrict?.Wards?.forEach { ward ->
                                        androidx.compose.material.DropdownMenuItem(
                                            onClick = {
                                                selectedWard = ward
                                                expandedWard = false
                                            }
                                        ) {
                                            Text(ward.Name)
                                        }
                                    }
                                }
                            }
                            TextField(
                                value = street,
                                onValueChange = { street = it },
                                label = {
                                    Text(
                                        text = "Tên đường, Tòa nhà, Số nhà",
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Đặt làm địa chỉ mặc định",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(
                                checked = defaultAddress,
                                onCheckedChange = {
                                    defaultAddress = it
                                },
                                modifier = Modifier,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.background,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.background,
                                    checkedBorderColor = Color.Gray,
                                    uncheckedBorderColor = Color.Gray
                                )
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            ) {
                Button(
                    onClick = {
                        if (name.isNotEmpty() && phoneNum.isNotEmpty() && selectedProvince != null && selectedDistrict != null && selectedWard != null && street.isNotEmpty()) {
                            isLoading = true
                            scope.launch {
                                GlobalRepository.addressRepository.addAddress(
                                    AddressModel.create(
                                        uid = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                                        name = name.trim(),
                                        phoneNum = phoneNum,
                                        province = selectedProvince!!.Name,
                                        district = selectedDistrict!!.Name,
                                        ward = selectedWard!!.Name,
                                        street = street.trim(),
                                        default = defaultAddress
                                    )
                                )
                                delay(200)
                                GlobalNavigation.navController.popBackStack()
                            }
                        } else {
                            AppUtil.showToast(context, "Vui lòng điền đầy đủ thông tin !!!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Hoàn thành",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun loadProvincesFromAssets(context: Context): List<Province> {
    val json = context.assets.open("data.json").bufferedReader().use { it.readText() }
    val type = object : TypeToken<List<Province>>() {}.type
    return Gson().fromJson(json, type)
}
