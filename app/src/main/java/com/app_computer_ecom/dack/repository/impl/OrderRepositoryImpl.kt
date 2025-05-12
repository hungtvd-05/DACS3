package com.app_computer_ecom.dack.repository.impl

import com.app_computer_ecom.dack.GlobalDatabase
import com.app_computer_ecom.dack.model.DailySales
import com.app_computer_ecom.dack.model.MonthlySales
import com.app_computer_ecom.dack.model.OrderModel
import com.app_computer_ecom.dack.model.ProductInfoModel
import com.app_computer_ecom.dack.model.ProductSoldInfo
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.app_computer_ecom.dack.repository.OrderRepository
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OrderRepositoryImpl : OrderRepository {
    var db = GlobalDatabase.database

    val dbOrder: CollectionReference = db.collection("orders")

    override suspend fun getOrdersByUidAndStatus(uid: String, status: Int): List<OrderModel> {
        return try {
            val querySnapshot = dbOrder
                .whereEqualTo("uid", uid)
                .whereEqualTo("status", status)
                .get()
                .await()
            if (querySnapshot.isEmpty) {
                emptyList()
            } else {
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(OrderModel::class.java)?.copy(id = document.id)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addOrder(orderModel: OrderModel) {
        dbOrder.add(orderModel)
        GlobalRepository.cartRepository.clearCart()
    }

    override suspend fun getOrdersOnAdmin(): Flow<List<OrderModel>> = callbackFlow {
        val listenerRegistration = dbOrder
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val orders = it.documents.mapNotNull { document ->
                        document.toObject(OrderModel::class.java)?.copy(id = document.id)
                    }
                    trySend(orders).isSuccess
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun getOrderById(orderId: String): OrderModel? {
        return try {
            val querySnapshot = dbOrder.document(orderId).get().await()
            if (!querySnapshot.exists()) {
                null
            } else {
                querySnapshot.toObject(OrderModel::class.java)?.copy(id = orderId)
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateOrderStatus(
        order: OrderModel,
        newStatus: Int,
        finishedAt: Timestamp
    ) {
        dbOrder.document(order.id).update("status", newStatus).await()
        val batch = Firebase.firestore.batch()
        batch.update(dbOrder.document(order.id), "finishedAt", finishedAt)
        if (newStatus == 3) {
            order.listProduct.forEach { productInfoModel ->
                var product =
                    GlobalRepository.productRepository.getProductById(productInfoModel.id) ?: run {
                        return@forEach
                    }
                val priceInfo =
                    product.prices.find { it.id == productInfoModel.selectType.id } ?: run {
                        return@forEach
                    }
                val newSold = priceInfo.sold + productInfoModel.quantity
                val newQuantity = priceInfo.quantity - productInfoModel.quantity
                if (newQuantity < 0) {
                    return@forEach
                }
                val priceIndex = product.prices.indexOf(priceInfo)
                product.prices[priceIndex] = priceInfo.copy(quantity = newQuantity, sold = newSold)

                val productRef = GlobalDatabase.database.collection("products").document(product.id)
                batch.set(productRef, product)
            }
        }
        batch.commit().await()
    }

    override suspend fun updateOrderListProduct(
        orderId: String,
        listProduct: List<ProductInfoModel>
    ) {
        dbOrder.document(orderId).update("listProduct", listProduct)
    }

    override suspend fun getDailySalesCurrentMonth(): List<DailySales> {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startTime = calendar.time

        val querySnapshot = dbOrder
            .whereGreaterThanOrEqualTo("createdAt", Timestamp(startTime))
            .get()
            .await()

        val orders = querySnapshot.toObjects(OrderModel::class.java)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val salesByDate = orders.groupBy { formatter.format(it.createdAt.toDate()) }
            .map { (date, orders) ->
                val doanhthudatduoc = orders.filter { it.status == 3 }.sumOf { it.totalPrice.toDouble() / 1000000 }
                val doanhthudukiendatthem = orders.sumOf { it.totalPrice.toDouble() / 1000000 } - doanhthudatduoc
                val donhanhhoanthanh = orders.filter { it.status == 3 }.size.toDouble()
                val donhangdangcho = orders.size.toDouble() - donhanhhoanthanh
                DailySales.create(
                    date,
                    donhangdangcho,
                    donhanhhoanthanh,
                    doanhthudukiendatthem,
                    doanhthudatduoc
                )
            }

        val result = mutableListOf<DailySales>()
        for (i in 0 until currentDay) {
            calendar.time = startTime
            calendar.add(Calendar.DAY_OF_MONTH, i)
            val dateStr = formatter.format(calendar.time)
            val dailySales = salesByDate.find { it.date == dateStr }
                ?: DailySales.create(
                    dateStr,
                    totalOrders = 0.0,
                    totalOrdersCompleted = 0.0,
                    totalExpectedSales = 0.0,
                    totalAchievedSales = 0.0
                )
            result.add(dailySales)
        }

        return result
    }

    override suspend fun getMonthlySalesCurrentYear(): List<MonthlySales> {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)

        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startTime = calendar.time

        val querySnapshot = dbOrder
            .whereGreaterThanOrEqualTo("createdAt", Timestamp(startTime))
            .get()
            .await()

        val orders = querySnapshot.toObjects(OrderModel::class.java)
        val formatter = SimpleDateFormat("MM/yy", Locale.getDefault())
        val salesByMonth = orders.groupBy { order ->
            formatter.format(order.createdAt.toDate())
        }.map { (month, orders) ->
            val doanhthudatduoc = orders.filter { it.status == 3 }.sumOf { it.totalPrice.toDouble() / 1000000 }
            val doanhthudukiendatthem = orders.sumOf { it.totalPrice.toDouble() / 1000000 } - doanhthudatduoc
            val donhanhhoanthanh = orders.filter { it.status == 3 }.size.toDouble()
            val donhangdangcho = orders.size.toDouble() - donhanhhoanthanh
            MonthlySales.create(
                month = month,
                totalOrders = donhangdangcho,
                totalOrdersCompleted = donhanhhoanthanh,
                totalExpectedSales = doanhthudukiendatthem,
                totalAchievedSales = doanhthudatduoc
            )
        }

        val result = mutableListOf<MonthlySales>()
        for (i in 0..currentMonth) {
            calendar.time = startTime
            calendar.add(Calendar.MONTH, i)
            val monthStr = formatter.format(calendar.time)
            val monthlySales = salesByMonth.find { it.month == monthStr }
                ?: MonthlySales.create(
                    month = monthStr,
                    totalOrders = 0.0,
                    totalOrdersCompleted = 0.0,
                    totalExpectedSales = 0.0,
                    totalAchievedSales = 0.0
                )
            result.add(monthlySales)
        }

        return result
    }

    override suspend fun getProductSoldQuantities(): List<ProductSoldInfo> {
        try {
            var productList = GlobalRepository.productRepository.getProducts()
            val productSoldInfoList = productList.map {
                ProductSoldInfo.create(
                    id = it.id,
                    name = it.name,
                    totalSold = it.prices.sumOf { priceInfo -> priceInfo.sold },
                    revenue = it.prices.sumOf { priceInfo -> priceInfo.price.toLong() * priceInfo.sold }
                )
            }
            return productSoldInfoList
        } catch (e: Exception) {
            return emptyList()
        }
    }
}