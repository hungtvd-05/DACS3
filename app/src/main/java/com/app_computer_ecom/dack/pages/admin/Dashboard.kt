package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.model.DailySales
import com.app_computer_ecom.dack.model.MonthlySales
import com.app_computer_ecom.dack.model.ProductSoldInfo
import com.app_computer_ecom.dack.repository.GlobalRepository
import java.text.NumberFormat
import java.util.Locale

@Composable
fun Dashboard(modifier: Modifier) {
    var dailySales by remember { mutableStateOf<List<DailySales>>(emptyList()) }
    var monthlySales by remember { mutableStateOf<List<MonthlySales>>(emptyList()) }
    var xAxisData by remember { mutableStateOf<List<String>>(emptyList()) }
    var y1AxisData by remember { mutableStateOf<List<Double>>(emptyList()) }
    var y2AxisData by remember { mutableStateOf<List<Double>>(emptyList()) }
    var listFilter = listOf("Theo ngày", "Theo tháng")
    var isLoading by remember { mutableStateOf(true) }

    var selectTypeSales by remember { mutableStateOf(0) }
    var productSoldQuantities by remember { mutableStateOf<List<ProductSoldInfo>>(emptyList()) }
    var totalOrders by remember { mutableStateOf<List<Double>>(emptyList()) }

    var totalOrdersCompleted by remember { mutableStateOf<List<Double>>(emptyList()) }
    var listFilterSold = listOf("Theo số lượng", "Theo doanh thu")
    var selectTypeSold by remember { mutableStateOf(0) }

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    LaunchedEffect(Unit) {
        dailySales = GlobalRepository.orderRepository.getDailySalesLast6Days()
        monthlySales = GlobalRepository.orderRepository.getDailySalesLast6Months()
        xAxisData = dailySales.map { it.date.substring(0, 5) }
        y1AxisData = dailySales.map { it.totalExpectedSales }
        y2AxisData = dailySales.map { it.totalAchievedSales }
        totalOrders = dailySales.map { it.totalOrders }
        totalOrdersCompleted = dailySales.map { it.totalOrdersCompleted }
        productSoldQuantities = GlobalRepository.orderRepository.getProductSoldQuantities().sortedByDescending { it.totalSold }

        isLoading = false
    }

    LaunchedEffect(selectTypeSales) {
        when {
            selectTypeSales == 0 -> {
                dailySales = GlobalRepository.orderRepository.getDailySalesLast6Days()
                xAxisData = dailySales.map { it.date.substring(0, 5) }
                y1AxisData = dailySales.map { it.totalExpectedSales }
                y2AxisData = dailySales.map { it.totalAchievedSales }
                totalOrders = dailySales.map { it.totalOrders }
                totalOrdersCompleted = dailySales.map { it.totalOrdersCompleted }
            }
            selectTypeSales == 1 -> {
                monthlySales = GlobalRepository.orderRepository.getDailySalesLast6Months()
                xAxisData = monthlySales.map { it.month }
                y1AxisData = monthlySales.map { it.totalExpectedSales }
                y2AxisData = monthlySales.map { it.totalAchievedSales }
                totalOrders = monthlySales.map { it.totalOrders }
                totalOrdersCompleted = monthlySales.map { it.totalOrdersCompleted }
            }
        }
    }

    LaunchedEffect(selectTypeSold) {
        when {
            selectTypeSold == 0 -> {
                productSoldQuantities = GlobalRepository.orderRepository.getProductSoldQuantities().sortedByDescending { it.totalSold }
            }
            selectTypeSold == 1 -> {
                productSoldQuantities = GlobalRepository.orderRepository.getProductSoldQuantities().sortedByDescending { it.revenue }
            }
        }
    }

    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "Doanh thu dự kiến",
            data = y1AxisData,
            lineColor = Color(0xFFFF7F50),
            lineType = LineType.DEFAULT_LINE,
            lineShadow = true
        ),
        LineParameters(
            label = "Doanh thu thực tế",
            data = y2AxisData,
            lineColor = Color(0xFF81BE88),
            lineType = LineType.DEFAULT_LINE,
            lineShadow = false,
        )
    )

    val testBarParameters: List<BarParameters> = listOf(
        BarParameters(
            dataName = "Số lượng đơn hàng",
            data = totalOrders,
            barColor = Color(0xFFF2BE22)
        ),
        BarParameters(
            dataName = "Số lượng đơn hàng hoàn tất",
            data = totalOrdersCompleted,
            barColor = Color(0xFFDFA878)
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "Thống kê",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Doanh thu"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        StatusFilterDropDownFun(
                            statusList = listFilter,
                            selected = selectTypeSales,
                            onStatusSelected = {
                                selectTypeSales = it
                            }
                        )

                    }
                }
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp)) {
                        LineChart(
                            modifier = Modifier.fillMaxSize() ,
                            linesParameters = testLineParameters,
                            isGrid = true,
                            gridColor = Color.Blue,
                            xAxisData = xAxisData,
                            animateChart = true,
                            showGridWithSpacer = true,
                            yAxisStyle = TextStyle(
                                fontSize = 8.sp,
                                color = Color.Gray,
                            ),
                            xAxisStyle = TextStyle(
                                fontSize = 8.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.W400
                            ),
                            yAxisRange = 5,
                            oneLineChart = false,
                            gridOrientation = GridOrientation.VERTICAL
                        )
                    }
                }
                item {
                    Box(Modifier.fillMaxWidth().height(400.dp)) {
                        BarChart(
                            chartParameters = testBarParameters,
                            gridColor = Color.DarkGray,
                            xAxisData = xAxisData,
                            isShowGrid = true,
                            animateChart = true,
                            showGridWithSpacer = true,
                            yAxisStyle = TextStyle(
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                            ),
                            xAxisStyle = TextStyle(
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.W400
                            ),
                            yAxisRange = maxOf(totalOrders.max(), totalOrdersCompleted.max()).toInt() + 1,
                            barWidth = 20.dp
                        )
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Sản phẩm"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        StatusFilterDropDownFun(
                            statusList = listFilterSold,
                            selected = selectTypeSold,
                            onStatusSelected = {
                                selectTypeSold = it
                            }
                        )
                    }
                }
                items(productSoldQuantities.size) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = productSoldQuantities[it].name)
                            Row {
                                Text(text = "Đã bán : ${productSoldQuantities[it].totalSold}")
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = "Doanh thu : ${formatter.format(productSoldQuantities[it].revenue)}")
                            }
                        }
                    }
                }
                item {  }
            }
        }
    }

}

@Composable
fun StatusFilterDropDownFun(
    statusList: List<String>,
    selected: Int,
    onStatusSelected: (Int) -> Unit,
) {
    var dropControl by remember { mutableStateOf(false) }
    var selectedStatus = statusList[selected]
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                if (statusList.isNotEmpty()) {
                    dropControl = true
                }
            },
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp)
                    .padding(5.dp)
            ) {
                Text(text = selectedStatus.toString())
                Icon(
                    imageVector = if (dropControl) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = ""
                )
            }
            DropdownMenu(
                expanded = dropControl,
                onDismissRequest = { dropControl = false },
                containerColor = Color.White
            ) {
                statusList.forEachIndexed { index, status ->
                    DropdownMenuItem(
                        text = { Text(status) },
                        onClick = {
                            onStatusSelected(index)
                            dropControl = false
                        },
                    )
                }
            }
        }
    }
}