package com.app_computer_ecom.dack.model

data class CartAndProductModel private constructor(
    val cartModel: CartModel,
    val productModel: ProductModel
) {
    companion object {
        fun create(
            cartModel: CartModel,
            productModel: ProductModel): CartAndProductModel {
            return CartAndProductModel(cartModel, productModel)
        }
    }
}
