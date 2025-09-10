package com.rahul.githubusersearchapp.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "theme_prefs")

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        // Load saved theme preference
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[DARK_MODE_KEY] ?: false
                }
                .collect { darkMode ->
                    _isDarkMode.value = darkMode
                }
        }
    }

    suspend fun toggleDarkMode() {
        val newMode = !_isDarkMode.value
        _isDarkMode.value = newMode

        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = newMode
        }
    }

    suspend fun setDarkMode(darkMode: Boolean) {
        _isDarkMode.value = darkMode

        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = darkMode
        }
    }
}