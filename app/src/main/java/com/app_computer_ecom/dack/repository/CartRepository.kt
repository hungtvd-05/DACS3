package com.app_computer_ecom.dack.repository

import android.content.Context
import com.app_computer_ecom.dack.model.CartModel
import com.app_computer_ecom.dack.model.PriceInfo

interface CartRepository {
    suspend fun getCartList(): Pair<List<CartModel>, Int>
    suspend fun getCartByUidAndPid(pid: String, selectType: PriceInfo): CartModel?
    suspend fun addToCart(context: Context, productId: String, selectType: PriceInfo)
    suspend fun updateCart(context: Context, cart: CartModel)
    suspend fun deleteCart(context: Context, cart: CartModel)
    suspend fun deleteCartByPid(pid: String)
    suspend fun totalPriceByUid(): Double
    suspend fun clearCart()
}