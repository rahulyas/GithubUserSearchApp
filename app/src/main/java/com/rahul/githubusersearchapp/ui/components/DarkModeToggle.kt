package com.rahul.githubusersearchapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.githubusersearchapp.R
import com.rahul.githubusersearchapp.presentation.viewmodel.MainViewModel
import com.rahul.githubusersearchapp.ui.theme.ThemeManager
import kotlinx.coroutines.launch

@Composable
fun DarkModeToggle(
    themeManager: ThemeManager = hiltViewModel<MainViewModel>().themeManager
) {
    val isDarkMode by themeManager.isDarkMode.collectAsState()
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                themeManager.toggleDarkMode()
            }
        }
    ) {
        Icon(
            painter = if (isDarkMode) painterResource(id = R.drawable.baseline_light_mode_24) else painterResource(id = R.drawable.baseline_dark_mode_24),
            contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}