package com.app_computer_ecom.dack.repository

import android.content.Context
import com.app_computer_ecom.dack.model.CartModel
import com.app_computer_ecom.dack.model.PriceInfo

interface CartRepository {
    suspend fun getCartList(): List<CartModel>
    suspend fun getCartByUidAndPid(pid: String, selectType: PriceInfo): CartModel?
    suspend fun addToCart(context: Context, productId: String, selectType: PriceInfo)
    suspend fun updateCart(context: Context, cart: CartModel)
    suspend fun deleteCart(context: Context, cart: CartModel)
}