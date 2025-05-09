package com.app_computer_ecom.dack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
//    primary = Color(0xFF536DFE),       // Xanh dương sáng cho nút, icon
    primary = Color(25, 118, 210),
    onPrimary = Color(0xFFFFFFFF),     // Trắng cho văn bản trên primary
    secondary = Color(0xFF26A69A),     // Xanh lam nhạt cho thành phần phụ
    onSecondary = Color(0xFFFFFFFF),   // Trắng trên secondary
    tertiary = Color(0xFFFFB300),      // Vàng cam cho điểm nhấn
    onTertiary = Color(0xFFFFFFFF),    // Trắng trên tertiary
    background = Color(0xFFF5F5F5),    // Xám nhẹ cho nền chính
    onBackground = Color(0xFF212121),  // Xám đen đậm cho văn bản trên nền
    surface = Color(0xFFFFFFFF),       // Trắng cho card, nhóm nội dung
    onSurface = Color(0xFF212121)      // Xám đen đậm cho văn bản trên surface
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFF4DB6AC),
    onSecondary = Color(0xFF000000),
    tertiary = Color(0xFFFFB300),
    onTertiary = Color(0xFF212121),
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF0E0E0E),
    onSurface = Color(0xFFFFFFFF)
)


@Composable
fun DACKTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = if (ThemeManager.isDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}