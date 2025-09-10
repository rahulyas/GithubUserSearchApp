package com.rahul.githubusersearchapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.githubusersearchapp.ui.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val themeManager: ThemeManager
) : ViewModel(){
    val isDarkMode = themeManager.isDarkMode

    fun toggleDarkMode() {
        viewModelScope.launch {
            themeManager.toggleDarkMode()
        }
    }

    fun setDarkMode(darkMode: Boolean) {
        viewModelScope.launch {
            themeManager.setDarkMode(darkMode)
        }
    }
}