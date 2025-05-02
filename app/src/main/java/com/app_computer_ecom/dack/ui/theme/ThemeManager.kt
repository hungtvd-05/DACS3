package com.app_computer_ecom.dack.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object ThemeManager {
    private val _isDarkTheme: MutableState<Boolean> = mutableStateOf(false)
    val isDarkTheme: Boolean get() = _isDarkTheme.value

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}