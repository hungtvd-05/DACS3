package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class OrderModel private constructor(
    val id: String = "",
    val uid: String = "",
    val email: String = "",
    val address: AddressModel = AddressModel(),
    val listProduct: List<ProductInfoModel> = emptyList(),
    val totalPrice: Int = 0,
    val paymentMethod: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val finishedAt: Timestamp = Timestamp.now(),
    var status: Int = 0,
) {
    companion object {
        fun create(
            id: String = "",
            uid: String = "",
            email: String = "",
            address: AddressModel = AddressModel(),
            listProduct: List<ProductInfoModel> = emptyList(),
            totalPrice: Int = 0,
            paymentMethod: String = "",
            createdAt: Timestamp = Timestamp.now(),
            finishedAt: Timestamp = Timestamp.now(),
            status: Int = 0,
        ): OrderModel {
            return OrderModel(
                id = id,
                uid = uid,
                email = email,
                address = address,
                listProduct = listProduct,
                totalPrice = totalPrice,
                paymentMethod = paymentMethod,
                createdAt = createdAt,
                finishedAt = finishedAt,
                status = status,
            )
        }
    }

    constructor(): this("", "", "", AddressModel(), emptyList(), 0, "", Timestamp.now(), Timestamp.now(), 0)
}
