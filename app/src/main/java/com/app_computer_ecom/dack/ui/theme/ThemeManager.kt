package com.app_computer_ecom.dack.ui.theme

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import com.app_computer_ecom.dack.data.datastore.SettingsKeys
import com.app_computer_ecom.dack.data.datastore.settingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object ThemeManager {
    private val _isDarkTheme: MutableState<Boolean> = mutableStateOf(false)
    val isDarkTheme: Boolean get() = _isDarkTheme.value

    fun toggleTheme(context: Context) {
        _isDarkTheme.value = !_isDarkTheme.value
        saveDarkMode(context, _isDarkTheme.value)
    }

    suspend fun initTheme(context: Context) {
        readDarkMode(context).firstOrNull()?.let {
            _isDarkTheme.value = it
        }
    }

    private fun readDarkMode(context: Context): Flow<Boolean> {
        return context.settingsDataStore.data
            .map { prefs -> prefs[SettingsKeys.DARK_MODE] ?: false }
    }

    private fun saveDarkMode(context: Context, enabled: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            context.settingsDataStore.edit { prefs ->
                prefs[SettingsKeys.DARK_MODE] = enabled
            }
        }
    }
}