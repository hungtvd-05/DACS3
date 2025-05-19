package com.app_computer_ecom.dack.screen.user

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.components.TopBar
import com.app_computer_ecom.dack.model.UserModel
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(fieldInt: Int) {

    val fields = listOf("họ và tên", "số điện thoại", "giới tính", "ngày sinh")

    val context = LocalContext.current

    var user = GLobalAuthViewModel.getAuthViewModel().userModel ?: UserModel()

    var name by remember { mutableStateOf(user.name) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var gender by remember { mutableStateOf(user.sex) }
    var birthDate by remember {
        mutableStateOf(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                user.birthDate.toDate()
            )
        )
    }

    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Nam", "Nữ", "Khác")

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            birthDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        TopBar(
            title = "Cập nhật ${fields.getOrElse(fieldInt) { "" }}"
        ) {
            GlobalNavigation.navController.popBackStack()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            when (fieldInt) {
                0 -> {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Họ và tên") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                1 -> {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }

                2 -> {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Giới tính") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        gender = option
                                        gender = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                3 -> {
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { },
                        label = { Text("Ngày sinh") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() },
                        enabled = false,
                        readOnly = true
                    )
                }

                else -> {
                    Text("Trường không hợp lệ")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {

                        when (fieldInt) {
                            0 -> {
                                GLobalAuthViewModel.getAuthViewModel().updateUserInfo(
                                    mapOf(
                                        "name" to name
                                    )
                                )
                            }

                            1 -> {
                                GLobalAuthViewModel.getAuthViewModel().updateUserInfo(
                                    mapOf(
                                        "phoneNumber" to phoneNumber
                                    )
                                )
                            }

                            2 -> {
                                GLobalAuthViewModel.getAuthViewModel().updateUserInfo(
                                    mapOf(
                                        "sex" to gender
                                    )
                                )
                            }

                            3 -> {
                                val date =
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(
                                        birthDate
                                    )
                                if (date != null) {
                                    GLobalAuthViewModel.getAuthViewModel().updateUserInfo(
                                        mapOf(
                                            "birthDate" to date
                                        )
                                    )
                                } else {
                                    AppUtil.showToast(context, "Ngày sinh không hợp lệ")
                                    return@launch
                                }
                            }
                        }

                        AppUtil.showToast(context, "Cập nhật thành công")
                        GlobalNavigation.navController.popBackStack()
                    }


                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lưu")
            }
        }
    }
}
