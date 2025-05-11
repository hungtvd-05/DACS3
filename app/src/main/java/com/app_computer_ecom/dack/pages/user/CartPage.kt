package com.app_computer_ecom.dack.pages.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.components.CartItemView
import com.app_computer_ecom.dack.model.CartModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartPage(modifier: Modifier = Modifier) {
    var cartList by remember { mutableStateOf(emptyList<CartModel>()) }
    var isLoading by remember { mutableStateOf(true) }
    var totalPrice by remember { mutableStateOf(0) }
    var data by remember { mutableStateOf<Pair<List<CartModel>, Int>?>(null) }
    val context = LocalContext.current
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        data = GlobalRepository.cartRepository.getCartList()
        if (data != null) {
            cartList = data!!.first
            totalPrice = data!!.second
        }
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Text(text = "Giỏ hàng", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))
        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                items(cartList, key = { it.id }) { item ->
                    CartItemView(
                        pid = item.pid,
                        quantity = item.quantity,
                        selectType = item.selectType,
                        delete = {
                            scope.launch {
                                GlobalRepository.cartRepository.deleteCart(context, item)
                                data = GlobalRepository.cartRepository.getCartList()
                                if (data != null) {
                                    cartList = data!!.first
                                    totalPrice = data!!.second
                                }
                            }
                        },
                        increse = {
                            scope.launch {
                                val index = cartList.indexOfFirst { it.id == item.id }
                                if (index != -1) {
//                                    val updatedItem = cartList[index].copy(quantity = cartList[index].quantity + 1)
//                                    cartList = cartList.toMutableList().apply {
//                                        this[index] = updatedItem
//                                    }
                                    GlobalRepository.cartRepository.updateCart(context, cartList[index].copy(quantity = cartList[index].quantity + 1))
                                    data = GlobalRepository.cartRepository.getCartList()
                                    if (data != null) {
                                        cartList = data!!.first
                                        totalPrice = data!!.second
                                    }
                                }
                            }
                        },
                        decrese = {
                            scope.launch {
                                val index = cartList.indexOfFirst { it.id == item.id }
                                if (index != -1) {
//                                    val updatedItem = cartList[index].copy(quantity = if ((cartList[index].quantity - 1) > 1) cartList[index].quantity - 1 else 1)
//                                    cartList = cartList.toMutableList().apply {
//                                        this[index] = updatedItem
//                                    }
                                    GlobalRepository.cartRepository.updateCart(context, cartList[index].copy(quantity = if ((cartList[index].quantity - 1) > 1) cartList[index].quantity - 1 else 1))
                                    data = GlobalRepository.cartRepository.getCartList()
                                    if (data != null) {
                                        cartList = data!!.first
                                        totalPrice = data!!.second
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Tổng cộng: ", fontSize = 12.sp)
                Text(text = formatter.format(totalPrice), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (cartList.isNotEmpty()) {
                            GlobalNavigation.navController.navigate("checkout")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(
                        text = "Mua hàng (${cartList.size})",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

    }
}