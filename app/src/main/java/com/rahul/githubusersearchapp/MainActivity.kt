package com.rahul.githubusersearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.rahul.githubusersearchapp.presentation.navigation.NavGraph
import com.rahul.githubusersearchapp.presentation.viewmodel.MainViewModel
import com.rahul.githubusersearchapp.ui.theme.GitHubUserSearchAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val isDarkMode by mainViewModel.isDarkMode.collectAsState()

            GitHubUserSearchAppTheme(
                darkTheme = isDarkMode
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(mainViewModel = mainViewModel, navController = navController)
                }
            }
        }
    }
}
