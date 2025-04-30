package com.app_computer_ecom.dack.pages.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.components.CartItemView
import com.app_computer_ecom.dack.model.CartModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch

@Composable
fun CartPage(modifier: Modifier = Modifier) {
    var cartList by remember { mutableStateOf(emptyList<CartModel>()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        cartList = GlobalRepository.cartRepository.getCartList()
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Giỏ hàng", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn {
                items(cartList, key = { it.id }) { item ->
                    CartItemView(
                        pid = item.pid,
                        quantity = item.quantity,
                        selectType = item.selectType,
                        delete = {
                            scope.launch {
                                GlobalRepository.cartRepository.deleteCart(context, item)
                                cartList = GlobalRepository.cartRepository.getCartList()
                            }
                        },
                        increse = {
                            scope.launch {
                                val index = cartList.indexOfFirst { it.id == item.id }
                                if (index != -1) {
                                    val updatedItem = cartList[index].copy(quantity = cartList[index].quantity + 1)
                                    cartList = cartList.toMutableList().apply {
                                        this[index] = updatedItem
                                    }
                                    GlobalRepository.cartRepository.updateCart(context, updatedItem)
                                }
                            }
                        },
                        decrese = {
                            scope.launch {
                                val index = cartList.indexOfFirst { it.id == item.id }
                                if (index != -1) {
                                    val updatedItem = cartList[index].copy(quantity = if ((cartList[index].quantity - 1) > 1) cartList[index].quantity - 1 else 1)
                                    cartList = cartList.toMutableList().apply {
                                        this[index] = updatedItem
                                    }
                                    GlobalRepository.cartRepository.updateCart(context, updatedItem)
                                }
                            }
                        }
                    )
                }
            }
        }

    }
}