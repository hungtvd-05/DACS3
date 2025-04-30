package com.app_computer_ecom.dack.repository.impl

import android.content.Context
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.BrandModel
import com.app_computer_ecom.dack.model.CartModel
import com.app_computer_ecom.dack.model.PriceInfo
import com.app_computer_ecom.dack.repository.CartRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class CartRepositoryImpl : CartRepository {
    var db = GlobalDatabase.database
    val dbCart: CollectionReference = db.collection("cart")

    override suspend fun getCartList(): List<CartModel> {
        return try {
            val querySnapshot = dbCart.get().await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(CartModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCartByUidAndPid(pid: String, selectType: PriceInfo): CartModel? {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null

            val querySnapshot = dbCart
                .whereEqualTo("uid", uid)
                .get()
                .await()

            querySnapshot.documents.firstOrNull { doc ->
                val cart = doc.toObject(CartModel::class.java)
                cart?.pid == pid && cart.selectType == selectType
            }?.let { doc ->
                doc.toObject(CartModel::class.java)?.copy(id = doc.id)
            }

        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addToCart(context: Context, productId: String, selectType: PriceInfo) {
        val cart = getCartByUidAndPid(productId, selectType)
        if (cart != null) {
            dbCart.document(cart.id).set(cart.copy(quantity = cart.quantity + 1))
        } else {
            dbCart.add(
                CartModel(
                    uid = FirebaseAuth.getInstance().currentUser?.uid!!,
                    pid = productId,
                    selectType = selectType,
                    quantity = 1
                )
            )
        }
        AppUtil.showToast(context, "Thêm vào giỏ hàng thành công")
    }

    override suspend fun updateCart(
        context: Context,
        cart: CartModel
    ) {
        dbCart.document(cart.id).set(cart)
    }

    override suspend fun deleteCart(context: Context, cart: CartModel) {
        dbCart.document(cart.id).delete()
        AppUtil.showToast(context, "Đã xóa khỏi giỏ hàng")
    }

    override suspend fun deleteCartByPid(pid: String) {
        val querySnapshot = dbCart
            .whereEqualTo("pid", pid)
            .get()
            .await()
        for (document in querySnapshot.documents) {
            dbCart.document(document.id).delete()
        }
    }
}