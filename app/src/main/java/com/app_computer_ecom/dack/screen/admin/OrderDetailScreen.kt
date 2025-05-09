package com.app_computer_ecom.dack.screen.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.model.ProductInfoModel
import com.app_computer_ecom.dack.pages.admin.StatusDropDownFun
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderDetailScreen(orderId: String) {
    BackHandler(enabled = true) {
        GlobalNavigation.navController.navigate("admin/2") {
            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    var order by remember { mutableStateOf<OrderModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var listStatus = listOf("Chờ xác nhận", "Chờ lấy hàng", "Chờ giao hàng", "Đã giao", "Đã hủy")

    val formatterDate = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    var selectedStatus by remember { mutableStateOf(0) }
    var lastSelectedStatus by remember { mutableStateOf(0) }
    var time by remember { mutableStateOf(Timestamp.now()) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        order = GlobalRepository.orderRepository.getOrderById(orderId)
        selectedStatus = order!!.status
        lastSelectedStatus = order!!.status
        time = order!!.finishedAt
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { GlobalNavigation.navController.navigate("admin/2") },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Chi tiết đơn hàng",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item { }
                item {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(
                                    if (lastSelectedStatus == 3) Color(46, 125, 50)
                                    else if (lastSelectedStatus == 4) Color(230, 81, 0)
                                    else Color(255, 171, 0)
                                ),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (lastSelectedStatus == 3) "Đơn hàng đã hoàn thành"
                                else if (lastSelectedStatus == 4) "Đơn hàng đã hủy"
                                else "Đơn hàng chưa hoàn thành",
                                modifier = Modifier.padding(8.dp),
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (lastSelectedStatus == 3) R.drawable.truck_tick_svgrepo_com__1_
                                    else if (lastSelectedStatus == 4) R.drawable.truck_remove_svgrepo_com
                                    else R.drawable.truck_time_svgrepo_com
                                ),
                                modifier = Modifier.height(30.dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Column(
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = listStatus[lastSelectedStatus],
                                    color = if (lastSelectedStatus == 3) Color(46, 125, 50)
                                    else if (lastSelectedStatus == 4) Color(230, 81, 0)
                                    else Color(255, 171, 0),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = formatterDate.format(
                                        time.toDate()
                                    ),
                                    fontSize = 12.sp,
                                    color = Color(158, 158, 158)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "Mã đơn hàng: ${order!!.id}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Ngày tạo đơn: ${formatterDate.format(order!!.createdAt.toDate())}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Ngày hoàn thành: ${
                                    if (lastSelectedStatus == 3 || lastSelectedStatus == 4) formatterDate.format(
                                        time.toDate()
                                    ) else ""
                                }",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Tên người nhận : ${order!!.address.name}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Số điện thoại : ${order!!.address.phoneNum}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Địa chỉ: ${order!!.address.street}, ${order!!.address.ward}, ${order!!.address.district}, ${order!!.address.province}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
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
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(top = 8.dp)
                        ) {
                            order!!.listProduct.forEachIndexed { index, it ->
                                ProductItemOrder(it)
                            }
                            Row {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Tổng số tiền (${order!!.listProduct.size} sản phẩm) : ",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "${formatter.format(order!!.totalPrice)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
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
                            modifier = Modifier.padding(vertical = 8.dp).padding(end = 8.dp)
                        ) {
                            StatusDropDownFun(
                                statusList = listStatus,
                                selected = selectedStatus,
                                lastSelected = lastSelectedStatus,
                                onStatusSelected = {
                                    selectedStatus = it
                                }
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = {
                                    scope.launch {
                                        if (selectedStatus != lastSelectedStatus) {
                                            lastSelectedStatus = selectedStatus
                                            time = Timestamp.now()
                                            GlobalRepository.orderRepository.updateOrderStatus(order!!, lastSelectedStatus, finishedAt = time)
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Text(text = "Cập nhật", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemOrder(productInfoModel: ProductInfoModel) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Row(
        modifier = Modifier.height(100.dp).padding(bottom = 8.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(Color.White)
        ) {
            AsyncImage(
                model = productInfoModel.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
            )
        }
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = productInfoModel.name,
                fontWeight = FontWeight.SemiBold,
                maxLines = 3,
                minLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .background(Color(233, 233, 233))
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = productInfoModel.selectType.type,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp, lineHeight = 12.sp,
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row {
                Text(
                    text = formatter.format(productInfoModel.selectType.price),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "x${productInfoModel.quantity}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}