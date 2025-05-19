package com.app_computer_ecom.dack.screen.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.components.TopBar
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.viewmodel.GLobalAuthViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


data class OrderStatus(
    val id: Int,
    val title: String,
)

@Composable
fun OrderStatusScreen(status: Int) {

    var orderStatus by remember { mutableIntStateOf(status) }

    val orderStatusList = listOf(
        OrderStatus(0, "Chờ xác nhận"),
        OrderStatus(1, "Chờ lấy hàng"),
        OrderStatus(2, "Chờ giao hàng"),
        OrderStatus(3, "Đã giao"),
        OrderStatus(4, "Đã huỷ"),
    )

    BackHandler {
        GlobalNavigation.navController.navigate("home/3")
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopBar(title = "Đơn hàng") {
                GlobalNavigation.navController.navigate("home/3")
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                OrderListWithFilter(orderStatusList, orderStatus) {
                    orderStatus = it
                }
            }
        }
    }
}

@Composable
fun OrderListWithFilter(
    orderStatusList: List<OrderStatus>,
    orderStatus: Int,
    onSetStatus: (status: Int) -> Unit
) {

    var orderModels by remember { mutableStateOf(emptyList<OrderModel>()) }
//    var orderProductInfoModels by remember { mutableStateOf(emptyList<OrderProductInfoModel>()) }

    var isLoading by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(orderStatus) {
        isLoading = true

        orderModels = GlobalRepository.orderRepository.getOrdersByUidAndStatus(
            uid = GLobalAuthViewModel.getAuthViewModel().userModel!!.uid,
            status = orderStatus
        ).sortedByDescending { it.createdAt.seconds }

//        orderProductInfoModels = orderModels.flatMap { order ->
//            order.listProduct.map { product ->
//                OrderProductInfoModel(
//                    id = order.id,
//                    uid = order.uid,
//                    address = order.address,
//                    totalPrice = order.totalPrice,
//                    paymentMethod = order.paymentMethod,
//                    createdAt = order.createdAt,
//                    finishedAt = order.finishedAt,
//                    status = order.status,
//                    product = product
//                )
//            }
//        }

        listState.animateScrollToItem(orderStatus)




        isLoading = false

    }


    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            state = listState
        ) {
            items(orderStatusList.size) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            onSetStatus(orderStatusList[it].id)
                        }
                        .padding(bottom = 4.dp)
                ) {
                    Text(
                        text = orderStatusList[it].title,
                        fontSize = 12.sp,
                        color = if (orderStatus == it)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onBackground,
                        fontWeight = if (orderStatus == it)
                            FontWeight.Bold
                        else
                            FontWeight.Normal,
                    )

                    if (orderStatus == it) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }

                }
            } else {
                if (orderModels.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "Chưa có đơn hàng nào",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                itemsIndexed(orderModels) { index, item ->
                    OrderProductItem(
                        orderModel = item,
                        onClick = {
                            if (item.status != 4) {
                                scope.launch {
                                    orderModels = orderModels.filter { it.id != item.id }
                                    var time = Timestamp.now()
                                    GlobalRepository.orderRepository.updateOrderStatus(
                                        item,
                                        4,
                                        finishedAt = time
                                    )
                                }
                            }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun OrderProductItem(orderModel: OrderModel, onClick: () -> Unit = {}) {

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    var listStatus = listOf("Chờ xác nhận", "Chờ lấy hàng", "Chờ giao hàng", "Đã giao", "Đã hủy")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(bottom = 16.dp)
            .clickable { GlobalNavigation.navController.navigate("order-details/orderId=${orderModel.id}") }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(100.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = orderModel.listProduct[0].imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                    placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
                )
            }
            Box() {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = orderModel.listProduct[0].name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = orderModel.listProduct[0].selectType.type,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "x" + orderModel.listProduct[0].quantity,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                Text(
                    text = formatter.format(orderModel.listProduct[0].selectType.price),
                    modifier = Modifier.align(Alignment.BottomEnd),
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Tổng số tiền (${orderModel.listProduct.size} sản phẩm) : ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(formatter.format(orderModel.totalPrice))
                    }
                },
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Địa chỉ nhận hàng",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                                append("${orderModel.address.name} | ")
                            }
                            withStyle(style = SpanStyle(color = Color.Gray, fontSize = 8.sp)) {
                                append(orderModel.address.phoneNum.toString())
                            }


                        },
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${orderModel.address.street}, ${orderModel.address.ward}, ${orderModel.address.district}, ${orderModel.address.province}",
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mã đơn hàng",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = orderModel.id,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Phương thức thanh toán",
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = orderModel.paymentMethod,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    color = Color.Gray
                )
            }


        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(horizontal = 16.dp),

            )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {

            Text(
                text = listStatus[orderModel.status],
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            TextButton(
                onClick = onClick
//                    {
//                        if (orderModel.status != 4) {
//                            scope.launch {
////                        lastSelectedStatus = 4
//                                var time = Timestamp.now()
//                                GlobalRepository.orderRepository.updateOrderStatus(
//                                    orderModel,
//                                    4,
//                                    finishedAt = time
//                                )
//                            }
//                        }
//                    }
                , contentPadding = PaddingValues(2.dp)
            ) {
                Text(
                    text = "Huỷ đơn",
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = if (orderModel.status == 4) Color.Gray else Color(230, 81, 0),
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    OrderStatusScreen(0)
}