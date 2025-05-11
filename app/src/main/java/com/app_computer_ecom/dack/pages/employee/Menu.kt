package com.app_computer_ecom.dack.pages.employee

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.pages.admin.BannerPage
import com.app_computer_ecom.dack.pages.admin.BrandPage
import com.app_computer_ecom.dack.pages.admin.CategoryPage
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

@Composable
fun MenuList(
    menuStatusList: List<MenuStatus>,
    menuStatus: Int,
    onSetStatus: (status: Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        items(menuStatusList.size) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        onSetStatus(menuStatusList[it].id)
                    }
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = menuStatusList[it].title,
                    fontSize = 12.sp,
                    color = if (menuStatus == it)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onBackground,
                    fontWeight = if (menuStatus == it)
                        FontWeight.Bold
                    else
                        FontWeight.Normal,
                )

                if (menuStatus == it) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(2.dp)
                            .width(32.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(1.dp)
                            )
                    )
                }
            }
        }
    }
    when (menuStatus) {
        0 -> ProductPage()
        1 -> CategoryPage()
        2 -> BrandPage()
        3 -> BannerPage()
    }

}