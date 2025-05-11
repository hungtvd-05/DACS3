package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.model.DailySales
import com.app_computer_ecom.dack.model.MonthlySales
import com.app_computer_ecom.dack.model.ProductSoldInfo
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.stacked
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


private val LegendLabelKey = ExtraStore.Key<Set<String>>()

@Composable
fun Dashboard(modifier: Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val modelOrder = remember { CartesianChartModelProducer() }
    var dailySales by remember { mutableStateOf<List<DailySales>>(emptyList()) }
    var monthlySales by remember { mutableStateOf<List<MonthlySales>>(emptyList()) }
    var listFilter = listOf("Theo ngày", "Theo tháng")
    var isLoading by remember { mutableStateOf(true) }

    var selectTypeSales by remember { mutableStateOf(0) }
    var productSoldQuantities by remember { mutableStateOf<List<ProductSoldInfo>>(emptyList()) }

    var listFilterSold = listOf("Theo số lượng", "Theo doanh thu")
    var selectTypeSold by remember { mutableStateOf(0) }

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    var labelSales = listOf<Number>()

    var sales = mapOf<String, List<Number>>()

    var orders = mapOf<String, List<Number>>()


    LaunchedEffect(Unit) {

        dailySales = GlobalRepository.orderRepository.getDailySalesCurrentMonth()
        labelSales = dailySales.map { it.date.substring(0, 2).toInt() }
        sales = mapOf(
            "Doanh thu đạt được" to dailySales.map { it.totalAchievedSales },
            "Doanh thu dự kiến đạt thêm" to dailySales.map { it.totalExpectedSales },
        )
        modelProducer.runTransaction {
            columnSeries { sales.values.forEach { series(labelSales, it) } }
            extras { it[LegendLabelKey] = sales.keys }
        }

        orders = mapOf(
            "Đơn hàng hoàn thành" to dailySales.map { it.totalOrdersCompleted },
            "Đơn hàng đang chờ" to dailySales.map { it.totalOrders },
        )
        modelOrder.runTransaction {
            columnSeries { orders.values.forEach { series(labelSales, it) } }
            extras { it[LegendLabelKey] = orders.keys }
        }

        productSoldQuantities = GlobalRepository.orderRepository.getProductSoldQuantities()
            .sortedByDescending { it.totalSold }

        isLoading = false
    }

    LaunchedEffect(selectTypeSales) {
        when {
            selectTypeSales == 0 -> {
                dailySales = GlobalRepository.orderRepository.getDailySalesCurrentMonth()
                labelSales = dailySales.map { it.date.substring(0, 2).toInt() }
                sales = mapOf(
                    "Doanh thu đạt được" to dailySales.map { it.totalAchievedSales },
                    "Doanh thu dự kiến đạt thêm" to dailySales.map { it.totalExpectedSales },
                )
                orders = mapOf(
                    "Đơn hàng hoàn thành" to dailySales.map { it.totalOrdersCompleted },
                    "Đơn hàng đang chờ" to dailySales.map { it.totalOrders },
                )
                modelOrder.runTransaction {
                    columnSeries { orders.values.forEach { series(labelSales, it) } }
                    extras { it[LegendLabelKey] = orders.keys }
                }
            }

            selectTypeSales == 1 -> {
                monthlySales = GlobalRepository.orderRepository.getMonthlySalesCurrentYear()
                labelSales = monthlySales.map { it.month.substring(0, 2).toInt() }
                sales = mapOf(
                    "Doanh thu đạt được" to monthlySales.map { it.totalAchievedSales },
                    "Doanh thu dự kiến đạt thêm" to monthlySales.map { it.totalExpectedSales },
                )
                orders = mapOf(
                    "Đơn hàng hoàn thành" to monthlySales.map { it.totalOrdersCompleted },
                    "Đơn hàng đang chờ" to monthlySales.map { it.totalOrders },
                )
                modelOrder.runTransaction {
                    columnSeries { orders.values.forEach { series(labelSales, it) } }
                    extras { it[LegendLabelKey] = orders.keys }
                }
            }
        }
        modelProducer.runTransaction {
            columnSeries { sales.values.forEach { series(labelSales, it) } }
            extras { it[LegendLabelKey] = sales.keys }
        }
    }

    LaunchedEffect(selectTypeSold) {
        when {
            selectTypeSold == 0 -> {
                productSoldQuantities = GlobalRepository.orderRepository.getProductSoldQuantities()
                    .sortedByDescending { it.totalSold }
            }

            selectTypeSold == 1 -> {
                productSoldQuantities = GlobalRepository.orderRepository.getProductSoldQuantities()
                    .sortedByDescending { it.revenue }
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "Thống kê",
            fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Doanh thu",
                            fontSize = 14.sp
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
                    SalesChart(modelProducer, Modifier)
                }
                item {
                    OrderChart(modelOrder)
                }
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Sản phẩm",
                            fontSize = 14.sp
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
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(productSoldQuantities.size) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.onTertiary)
                            .padding(8.dp),
                    ) {
                        Text(text = productSoldQuantities[it].name, fontSize = 12.sp)
                        Row {
                            Text(text = "Đã bán : ${productSoldQuantities[it].totalSold}", fontSize = 12.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "Doanh thu : ${formatter.format(productSoldQuantities[it].revenue)}", fontSize = 12.sp)
                        }
                    }
                }
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiary)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp)
                    .padding(5.dp)
            ) {
                Text(text = selectedStatus.toString(), fontSize = 12.sp)
                Icon(
                    imageVector = if (dropControl) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = ""
                )
            }
            DropdownMenu(
                expanded = dropControl,
                onDismissRequest = { dropControl = false },
                containerColor = MaterialTheme.colorScheme.onTertiary
            ) {
                statusList.forEachIndexed { index, status ->
                    DropdownMenuItem(
                        text = { Text(status, fontSize = 12.sp) },
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

@Composable
fun SalesChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    val YDecimalFormat = DecimalFormat("#.### tr")
    val StartAxisValueFormatter = CartesianValueFormatter.decimal(YDecimalFormat)
    val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ 2.0 })
    val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

    val columnColors = listOf(Color(0xff6438a7), Color(0xff3490de), Color(0xff73e8dc))
    val legendItemLabelComponent = rememberTextComponent(MaterialTheme.colorScheme.onBackground, textSize = 12.sp)
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider =
                        ColumnCartesianLayer.ColumnProvider.series(
                            columnColors.map { color ->
                                rememberLineComponent(fill = fill(color), thickness = 16.dp)
                            }
                        ),
                    columnCollectionSpacing = 32.dp,
                    mergeMode = { ColumnCartesianLayer.MergeMode.stacked() },
                ),
                startAxis =
                    VerticalAxis.rememberStart(
                        valueFormatter = StartAxisValueFormatter,
                        itemPlacer = StartAxisItemPlacer,
                        label = legendItemLabelComponent
                    ),
                bottomAxis =
                    HorizontalAxis.rememberBottom(
                        itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                        label = legendItemLabelComponent
                    ),
                marker = rememberMarker(MarkerValueFormatter),
                layerPadding = {
                    cartesianLayerPadding(
                        scalableStart = 16.dp,
                        scalableEnd = 16.dp
                    )
                },
                legend =
                    rememberHorizontalLegend(
                        items = { extraStore ->
                            extraStore[LegendLabelKey].forEachIndexed { index, label ->
                                add(
                                    LegendItem(
                                        shapeComponent(
                                            fill(columnColors[index]),
                                            CorneredShape.Pill
                                        ),
                                        legendItemLabelComponent,
                                        label,
                                    )
                                )
                            }
                        },
                        padding = insets(top = 16.dp),
                    ),
            ),
        modelProducer = modelProducer,
        modifier = modifier.height(252.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true),
    )
}

@Composable
fun OrderChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    val YDecimalFormat = DecimalFormat()
    val StartAxisValueFormatter = CartesianValueFormatter.decimal(YDecimalFormat)
    val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ 1.0 })
    val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

    val columnColors = listOf(Color(0xff6438a7), Color(0xff3490de), Color(0xff73e8dc))
    val legendItemLabelComponent = rememberTextComponent(color = MaterialTheme.colorScheme.onBackground, textSize = 12.sp)
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider =
                        ColumnCartesianLayer.ColumnProvider.series(
                            columnColors.map { color ->
                                rememberLineComponent(fill = fill(color), thickness = 16.dp)
                            }
                        ),
                    columnCollectionSpacing = 32.dp,
                    mergeMode = { ColumnCartesianLayer.MergeMode.stacked() },
                ),
                startAxis =
                    VerticalAxis.rememberStart(
                        valueFormatter = StartAxisValueFormatter,
                        itemPlacer = StartAxisItemPlacer,
                        label = legendItemLabelComponent
                    ),
                bottomAxis =
                    HorizontalAxis.rememberBottom(
                        itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                        label = legendItemLabelComponent,
                    ),
                marker = rememberMarker(MarkerValueFormatter),
                layerPadding = {
                    cartesianLayerPadding(
                        scalableStart = 16.dp,
                        scalableEnd = 16.dp
                    )
                },
                legend =
                    rememberHorizontalLegend(
                        items = { extraStore ->
                            extraStore[LegendLabelKey].forEachIndexed { index, label ->
                                add(
                                    LegendItem(
                                        shapeComponent(
                                            fill(columnColors[index]),
                                            CorneredShape.Pill
                                        ),
                                        legendItemLabelComponent,
                                        label,
                                    )
                                )
                            }
                        },
                        padding = insets(top = 16.dp),
                    ),
            ),
        modelProducer = modelProducer,
        modifier = modifier.height(252.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true),
    )
}