package com.app_computer_ecom.dack.pages.employee

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app_computer_ecom.dack.pages.admin.MenuList
import com.app_computer_ecom.dack.pages.admin.MenuStatus

@Composable
fun Menu(modifier: Modifier = Modifier) {
    val orderStatusList = listOf(
        MenuStatus(0, "Sản phẩm"),
        MenuStatus(1, "Danh mục"),
        MenuStatus(2, "Thương hiệu"),
        MenuStatus(3, "Banner"),
    )

    var orderStatus by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MenuList(orderStatusList, orderStatus) {
                orderStatus = it
            }
        }
    }
}

