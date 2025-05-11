package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderPage(modifier: Modifier = Modifier) {

    var orderList by remember { mutableStateOf(emptyList<OrderModel>()) }
    var isLoading by remember { mutableStateOf(true) }
    var listStatus = listOf("Chờ xác nhận", "Chờ lấy hàng", "Chờ giao hàng", "Đã giao", "Đã hủy")

    LaunchedEffect(Unit) {
        orderList = GlobalRepository.orderRepository.getOrdersOnAdmin()
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp)
    ) {
        Text(text = "Đơn hàng", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))
        if (isLoading) {
            LoadingScreen()
        } else {
            if (orderList.isEmpty()) {
                Text(text = "Không có đơn hàng nào !!", fontSize = 14.sp)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {  }
                    items(orderList.size) { index ->
                        ItemOrder(order = orderList[index], listStatus = listStatus)
                    }
                    item {  }
                }
            }
        }
    }
}

@Composable
fun ItemOrder(order: OrderModel, listStatus: List<String>) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    val scope = rememberCoroutineScope()

    var selectedStatus by remember { mutableStateOf(order.status) }
    var lastSelectedStatus by remember { mutableStateOf(order.status) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .clickable(
                onClick = {
                    GlobalNavigation.navController.navigate("employee/orderdetail/id=${order.id}")
                }
            )
    ) {
        Text(
            text = "Mã đơn hàng: ${order.id}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Địa chỉ nhận hàng",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                append("${order.address.name} | ")
                            }
                            withStyle(style = SpanStyle(color = Color.Gray, fontSize = 8.sp)) {
                                append(order.address.phoneNum)
                            }
                        },
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${order.address.street}, ${order.address.ward}, ${order.address.district}, ${order.address.province}",
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trạng thái : ",
                fontSize = 12.sp
            )
            Text(
                text = listStatus[lastSelectedStatus],
                color = if (order.status == 3) Color(46, 125, 50)
                else if (order.status == 4) Color(230, 81, 0)
                else Color(255, 171, 0),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Phương thức thanh toán : ",
                fontSize = 12.sp
            )
            Text(
                text = order.paymentMethod,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.background
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.height(100.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = order.listProduct.firstOrNull()?.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                    placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
                )
            }
            Column(
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = order.listProduct[0].name,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color(233, 233, 233))
                    ) {
                        Text(
                            text = order.listProduct[0].selectType.type,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 10.sp, lineHeight = 12.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "x${order.listProduct[0].quantity}",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }

                Text(
                    text = "${formatter.format(order.listProduct[0].selectType.price)}",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Tổng số tiền (${order.listProduct.size} sản phẩm) : ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(formatter.format(order.totalPrice))
                    }
                },
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            StatusDropDownFun(
                statusList = listStatus,
                selected = selectedStatus,
                lastSelected = lastSelectedStatus,
                onStatusSelected = {
                    selectedStatus = it
                }
            )
            Button(
                onClick = {
                    scope.launch {
                        if (selectedStatus != lastSelectedStatus) {
                            lastSelectedStatus = selectedStatus
                            GlobalRepository.orderRepository.updateOrderStatus(order, lastSelectedStatus)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Text(text = "Cập nhật", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun StatusDropDownFun(
    statusList: List<String>,
    selected: Int,
    lastSelected: Int,
    onStatusSelected: (Int) -> Unit,
) {
    var dropControl by remember { mutableStateOf(false) }
    var selectedStatus = statusList[selected]
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                if (statusList.isNotEmpty()) {
                    dropControl = true
                }
            },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp)
                    .padding(5.dp)
            ) {
                Text(text = selectedStatus.toString(), fontSize = 12.sp)
                Icon(
                    imageVector = if (dropControl) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = ""
                )
            }
            DropdownMenu(
                expanded = dropControl,
                onDismissRequest = { dropControl = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                if (lastSelected == 3) {
                    DropdownMenuItem(
                        text = { Text("Đã giao", fontSize = 12.sp) },
                        onClick = {
                            dropControl = false
                        },
                    )
                } else if (lastSelected == 4) {
                    DropdownMenuItem(
                        text = { Text("Đã hủy", fontSize = 12.sp) },
                        onClick = {
                            dropControl = false
                        },
                    )
                } else {
                    statusList.forEachIndexed { index, status ->
                        DropdownMenuItem(
                            text = { Text(status, fontSize = 12.sp) },
                            onClick = {
                                onStatusSelected(index)
                                dropControl = false
                            },
                        )
                    }
                }
            }
        }
    }
}
