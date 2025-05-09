package com.app_computer_ecom.dack.pages.admin

import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import java.text.DecimalFormat

private val LegendLabelKey = ExtraStore.Key<Set<String>>()
private val YDecimalFormat = DecimalFormat("#.## h")
private val StartAxisValueFormatter = CartesianValueFormatter.decimal(YDecimalFormat)
private val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ 0.5 })
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

//@Composable
//fun JetpackComposeDailyDigitalMediaUse(
//    modelProducer: CartesianChartModelProducer,
//    modifier: Modifier = Modifier,
//) {
//    val columnColors = listOf(Color(0xff6438a7), Color(0xff3490de), Color(0xff73e8dc))
//    val legendItemLabelComponent = rememberTextComponent(Color.Black)
//    CartesianChartHost(
//        chart =
//            rememberCartesianChart(
//                rememberColumnCartesianLayer(
//                    columnProvider =
//                        ColumnCartesianLayer.ColumnProvider.series(
//                            columnColors.map { color ->
//                                rememberLineComponent(fill = fill(color), thickness = 16.dp)
//                            }
//                        ),
//                    columnCollectionSpacing = 32.dp,
//                    mergeMode = { ColumnCartesianLayer.MergeMode.stacked() },
//                ),
//                startAxis =
//                    VerticalAxis.rememberStart(
//                        valueFormatter = StartAxisValueFormatter,
//                        itemPlacer = StartAxisItemPlacer,
//                        label = legendItemLabelComponent
//                    ),
//                bottomAxis =
//                    HorizontalAxis.rememberBottom(
//                        itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
//                        label = legendItemLabelComponent
//                    ),
//                marker = rememberMarker(MarkerValueFormatter),
//                layerPadding = { cartesianLayerPadding(scalableStart = 16.dp, scalableEnd = 16.dp) },
//                legend =
//                    rememberHorizontalLegend(
//                        items = { extraStore ->
//                            extraStore[LegendLabelKey].forEachIndexed { index, label ->
//                                add(
//                                    LegendItem(
//                                        shapeComponent(fill(columnColors[index]), CorneredShape.Pill),
//                                        legendItemLabelComponent,
//                                        label,
//                                    )
//                                )
//                            }
//                        },
//                        padding = insets(top = 16.dp),
//                    ),
//            ),
//        modelProducer = modelProducer,
//        modifier = modifier.height(252.dp),
//        zoomState = rememberVicoZoomState(zoomEnabled = false),
//    )
//}

//private val x = (2008..2018).toList()
//
//private val y =
//    mapOf(
//        "Laptop/desktop" to listOf<Number>(2.2, 2.3, 2.4, 2.6, 2.5, 2.3, 2.2, 2.2, 2.2, 2.1, 2),
//        "Mobile" to listOf(0.3, 0.3, 0.4, 0.8, 1.6, 2.3, 2.6, 2.8, 3.1, 3.3, 3.6),
////        "Other" to listOf(0.2, 0.3, 0.4, 0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.6, 0.7),
//    )
//
//
//@Composable
//fun JetpackComposeDailyDigitalMediaUse(modifier: Modifier = Modifier, x: List<Number>, y: Map<String, List<Number>>) {
//    val modelProducer = remember { CartesianChartModelProducer() }
//    var x1 = listOf<Number>()
//    var y1 = mapOf<String, List<Number>>()
//    var dailySales by remember { mutableStateOf<List<DailySales>>(emptyList()) }
////    x1 = (2008..2018).toList()
////    y1 = mapOf(
////        "Laptop/desktop" to listOf<Number>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
////        "Mobile" to listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
//////        "Other" to listOf(0.2, 0.3, 0.4, 0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.6, 0.7),
////    )
//    LaunchedEffect(Unit) {
//        dailySales = GlobalRepository.orderRepository.getDailySalesLast6Days()
//        x1 = dailySales.map { it.date.substring(0, 2).toInt() }
//        y1 = mapOf(
//            "Doanh thu đạt được" to dailySales.map { it.totalAchievedSales },
//            "Mobile" to dailySales.map { it.totalExpectedSales },
//        )
//        modelProducer.runTransaction {
//            columnSeries { y1.values.forEach { series(x1, it) } }
//            extras { it[LegendLabelKey] = y1.keys }
//        }
//
//    }
//    JetpackComposeDailyDigitalMediaUse(modelProducer, modifier)
//}
