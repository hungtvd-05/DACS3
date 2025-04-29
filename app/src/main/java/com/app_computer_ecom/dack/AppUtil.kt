package com.app_computer_ecom.dack

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

object AppUtil {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun addToCart(context: Context, productId: String) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val newQuantity = currentQuantity + 1

                val updateCart = mapOf("cartItems.$productId" to newQuantity)

                userDoc.update(updateCart).addOnCompleteListener {
                    if (it.isSuccessful) {
                        AppUtil.showToast(context, "Thêm vào giỏ hàng thành công")
                    } else {
                        AppUtil.showToast(context, "Thêm vào giỏ hàng thất bại")
                    }
                }
            }
        }
    }
}