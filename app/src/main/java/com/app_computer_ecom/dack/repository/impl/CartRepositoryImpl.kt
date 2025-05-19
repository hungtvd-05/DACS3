package com.app_computer_ecom.dack.repository.impl

import android.content.Context
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.CartAndProductModel
import com.app_computer_ecom.dack.model.CartModel
import com.app_computer_ecom.dack.model.PriceInfo
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.CartRepository
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class CartRepositoryImpl : CartRepository {
    var db = GlobalDatabase.database
    val dbCart: CollectionReference = db.collection("cart")
    val dbProduct: CollectionReference = db.collection("products")

    override suspend fun getCartList(): Pair<List<CartAndProductModel>, Int> {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val querySnapshot = dbCart.whereEqualTo("uid", uid).get().await()
            if (querySnapshot.isEmpty) {
                return Pair(emptyList(), 0)
            }

            val pids = querySnapshot.documents.mapNotNull { it.toObject(CartModel::class.java)?.pid }.distinct()
            val productMap = if (pids.isNotEmpty()) {
                val chunkedPids = pids.chunked(10)
                val documents = mutableListOf<DocumentSnapshot>()
                chunkedPids.forEach { chunk ->
                    documents.addAll(
                        dbProduct.whereIn(FieldPath.documentId(), chunk).get().await().documents
                    )
                }
                documents.mapNotNull { doc -> doc.toObject(ProductModel::class.java)?.let { doc.id to it } }.toMap()
            } else {
                emptyMap()
            }

            val validCarts = mutableListOf<CartAndProductModel>()
            val docIdsToDelete = mutableListOf<String>()

            for (document in querySnapshot.documents) {
                val cart = document.toObject(CartModel::class.java)?.copy(id = document.id)
                if (cart != null) {
                    val product = productMap[cart.pid] ?: GlobalRepository.productRepository.getProductById(cart.pid)
                    if (product != null && product.prices.find { it.id == cart.selectType.id }?.quantity?.let { it >= cart.quantity } == true) {
                        validCarts.add(CartAndProductModel.create(cart, product))
                    } else {
                        docIdsToDelete.add(cart.id)
                    }
                }
            }

            if (docIdsToDelete.isNotEmpty()) {
                val batch = dbCart.firestore.batch()
                docIdsToDelete.distinct().forEach { docId ->
                    batch.delete(dbCart.document(docId))
                }
                batch.commit().await()
            }

            val totalPrice = validCarts.sumOf { it.cartModel.selectType.price * it.cartModel.quantity }
            Pair(validCarts, totalPrice)
        } catch (e: Exception) {
            Pair(emptyList(), 0)
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

    override suspend fun totalPrices(): Int {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val querySnapshot = dbCart.whereEqualTo("uid", uid).get().await()
            if (querySnapshot.isEmpty) {
                0
            } else {
                val cartList = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(CartModel::class.java)?.copy(id = document.id)
                }
                val totalPrice = cartList.sumOf { it.selectType.price * it.quantity }
                totalPrice
            }
        } catch (e: Exception) {
            0
        }
    }

    override suspend fun addToCart(context: Context, productId: String, selectType: PriceInfo) {
        val cart = getCartByUidAndPid(productId, selectType)
        if (cart != null) {
            dbCart.document(cart.id).set(cart.copy(quantity = cart.quantity + 1))
        } else {
            dbCart.add(
                CartModel.create(
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
        cart: CartModel
    ) {
        dbCart.document(cart.id).update("quantity", cart.quantity)
    }

    override suspend fun deleteCart(context: Context, cart: CartModel) {
        dbCart.document(cart.id).delete()
        AppUtil.showToast(context, "Đã xóa khỏi giỏ hàng")
    }

    override suspend fun clearCart() {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: throw IllegalStateException()
            val batch = Firebase.firestore.batch()
            val querySnapshot = dbCart
                .whereEqualTo("uid", uid)
                .get()
                .await()
            for (document in querySnapshot.documents) {
                batch.delete(dbCart.document(document.id))
            }
            batch.commit().await()
        } catch (e: Exception) {

        }
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