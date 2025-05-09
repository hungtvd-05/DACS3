package com.app_computer_ecom.dack.screen.user

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.components.CartItemViewOnCheckOut
import com.app_computer_ecom.dack.model.AddressModel
import com.app_computer_ecom.dack.model.CartModel
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.model.ProductInfoModel
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.pages.user.Loading
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.zalopay.Api.CreateOrder
import kotlinx.coroutines.launch
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen() {

    BackHandler(enabled = true) {
        GlobalNavigation.navController.navigate("home/2") {
            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    var address: AddressModel? by remember { mutableStateOf(AddressModel()) }
    var cartList by remember { mutableStateOf(emptyList<CartModel>()) }
    var totalPrice by remember { mutableStateOf(0) }
    var data by remember { mutableStateOf<Pair<List<CartModel>, Int>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val context = LocalContext.current
    var tempProduct by remember { mutableStateOf<ProductModel?>(null) }
    var listProduct by remember { mutableStateOf(emptyList<ProductModel>()) }
    val scope = rememberCoroutineScope()

    var selectPayment by remember { mutableStateOf(false) }
    var zpTransToken by remember { mutableStateOf<String?>(null) }
    var isCreatingOrder by remember { mutableStateOf(false) }

    val activity = remember(context) {
        when (context) {
            is Activity -> context
            else -> null
        }
    }

    LaunchedEffect(Unit) {
        address = GlobalRepository.addressRepository.getDefaultAddress()
        data = GlobalRepository.cartRepository.getCartList()
        if (data != null) {
            cartList = data!!.first
            totalPrice = data!!.second
        }
        for (cart in cartList) {
            tempProduct = GlobalRepository.productRepository.getProductById(cart.pid)
            if (tempProduct != null) {
                listProduct = listProduct + tempProduct!!
                tempProduct = null
            }
        }
        isLoading = false
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            IconButton(
                onClick = { GlobalNavigation.navController.navigate("home/2") },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Thanh toán",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isLoading || isCreatingOrder) {
            Loading()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {

                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            GlobalNavigation.navController.navigate("menuaddress")
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location"
                            )
                            if (address != null) {
                                Column {
                                    Text(
                                        text = "${address!!.name} | ${address!!.phoneNum}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${address!!.street}, ${address!!.ward}, ${address!!.district}, ${address!!.province}",
                                        fontSize = 12.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "Vui lòng chọn địa chỉ !!",
                                    fontSize = 12.sp
                                )
                            }
                        }

                    }
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(top = 8.dp)
                        ) {
                            cartList.forEachIndexed { index, it ->
                                CartItemViewOnCheckOut(
                                    product = listProduct[index],
                                    quantity = it.quantity,
                                    selectType = it.selectType
                                )
                            }
                        }
                    }
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Phương thức thanh toán : "
                            )
                            Column(modifier = Modifier.selectableGroup()) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = !selectPayment,
                                        onClick = {
                                            selectPayment = false
                                        }
                                    )
                                    Text(
                                        text = "Thanh toán khi nhận hàng",
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectPayment,
                                        onClick = {
                                            selectPayment = true
                                        }
                                    )
                                    Text(
                                        text = "Thanh toán bằng thẻ tín dụng",
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                item { }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Tổng cộng: ", fontSize = 14.sp)
                    Text(
                        text = formatter.format(totalPrice),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(25, 118, 210)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            if (address == null) {
                                AppUtil.showToast(context, "Vui lòng chọn địa chỉ !!")
                            } else {
                                if (selectPayment) {
                                    if (activity == null) {
                                        AppUtil.showToast(context, "Không thể truy cập Activity")
                                        Log.e("ZaloPay", "Activity is null")
                                        return@Button
                                    }
                                    isCreatingOrder = true
                                    scope.launch {

                                        try {
                                            val orderApi = CreateOrder()
                                            val data = orderApi.createOrder(totalPrice.toString())
                                            val code = data.getString("returncode")
//                                            Log.d("ZaloPay", "CreateOrder response: $data")
                                            if (code == "1") {
                                                zpTransToken = data.getString("zptranstoken")
                                                // Gọi thanh toán ngay sau khi tạo đơn hàng
                                                ZaloPaySDK.getInstance().payOrder(
                                                    activity,
                                                    zpTransToken!!,
                                                    "demozpdk://app",
                                                    object : PayOrderListener {
                                                        override fun onPaymentSucceeded(
                                                            transactionId: String,
                                                            transToken: String,
                                                            appTransID: String
                                                        ) {
                                                            AppUtil.showToast(
                                                                context,
                                                                "Thanh toán thành công: TransactionId=$transactionId"
                                                            )
                                                            scope.launch {
                                                                val newProductList = mutableListOf<ProductInfoModel>()
                                                                listProduct.forEachIndexed { index, it ->
                                                                    newProductList.add(
                                                                        ProductInfoModel(
                                                                            id = it.id,
                                                                            name = it.name,
                                                                            imageUrl = it.imageUrls.firstOrNull()?.imageUrl ?: "",
                                                                            selectType = cartList[index].selectType,
                                                                            quantity = cartList[index].quantity
                                                                        )
                                                                    )
                                                                }
                                                                GlobalRepository.orderRepository.addOrder(
                                                                    OrderModel(
                                                                        uid = address!!.uid,
                                                                        address = address!!,
                                                                        listProduct = newProductList,
                                                                        totalPrice = totalPrice,
                                                                        paymentMethod = "ZaloPay"
                                                                    )
                                                                )
                                                                GlobalNavigation.navController.navigate("order-success")
                                                            }
                                                            isCreatingOrder = false
                                                        }

                                                        override fun onPaymentCanceled(
                                                            zpTransToken: String,
                                                            appTransID: String
                                                        ) {
                                                            AppUtil.showToast(
                                                                context,
                                                                "Thanh toán bị hủy"
                                                            )
                                                            isCreatingOrder = false
                                                        }

                                                        override fun onPaymentError(
                                                            zaloPayError: ZaloPayError,
                                                            zpTransToken: String,
                                                            appTransID: String
                                                        ) {
                                                            AppUtil.showToast(
                                                                context,
                                                                "Thanh toán thất bại: ${zaloPayError.name}"
                                                            )
//                                                            Log.e(
//                                                                "ZaloPay",
//                                                                "Payment error: ${zaloPayError.name}, zpTransToken: $zpTransToken"
//                                                            )
                                                            isCreatingOrder = false
                                                        }
                                                    }
                                                )
                                            } else {
                                                val message = data.optString("returnmessage", "Lỗi không xác định")
                                                AppUtil.showToast(context, "Tạo đơn hàng thất bại: $message")
//                                                Log.e("ZaloPay", "CreateOrder failed with code: $code, message: $message")
                                                isCreatingOrder = false
                                            }
                                        } catch (e: Exception) {
                                            AppUtil.showToast(context, "Lỗi khi tạo đơn hàng: ${e.message}")
                                            Log.e("ZaloPay", "CreateOrder error: ${e.message}")
                                            isCreatingOrder = false
                                        }
                                    }
                                } else {
                                    isLoading = true
                                    scope.launch {
                                        val newProductList = mutableListOf<ProductInfoModel>()
                                        listProduct.forEachIndexed { index, it ->
                                            newProductList.add(
                                                ProductInfoModel(
                                                    id = it.id,
                                                    name = it.name,
                                                    imageUrl = it.imageUrls.firstOrNull()!!.imageUrl,
                                                    selectType = cartList.get(index).selectType,
                                                    quantity = cartList.get(index).quantity
                                                )
                                            )
                                        }
                                        GlobalRepository.orderRepository.addOrder(
                                            OrderModel(
                                                uid = address!!.uid,
                                                address = address!!,
                                                listProduct = newProductList,
                                                totalPrice = totalPrice,
                                                paymentMethod = "COD"
                                            )
                                        )
                                    }
                                    GlobalNavigation.navController.navigate("order-success")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(25, 118, 210),
                        )
                    ) {
                        Text(
                            text = "Đặt hàng"
                        )
                    }
                }
            }
        }
    }
}



