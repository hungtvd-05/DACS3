package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class OrderModel(
    val id: String = "",
    val uid: String = "",
    val address: AddressModel = AddressModel(),
    val listProduct: List<ProductInfoModel> = emptyList(),
    val totalPrice: Int = 0,
    val paymentMethod: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val finishedAt: Timestamp = Timestamp.now(),
    var status: Int = 0,
)
