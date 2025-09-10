package com.rahul.githubusersearchapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rahul.githubusersearchapp.presentation.viewmodel.MainViewModel
import com.rahul.githubusersearchapp.ui.screens.ProfileScreen
import com.rahul.githubusersearchapp.ui.screens.RepositoriesScreen
import com.rahul.githubusersearchapp.ui.screens.RepositoryDetailScreen
import com.rahul.githubusersearchapp.ui.screens.SearchScreen

@Composable
fun NavGraph(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ) {
        composable(Screen.Search.route) {
            SearchScreen(mainViewModel = mainViewModel, navController = navController)
        }
        composable(
            route = Screen.Profile.route,
            arguments = listOf(
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            ProfileScreen(username = username, navController = navController)
        }

        composable(
            route = Screen.Repositories.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            RepositoriesScreen(
                username = username,
                navController = navController
            )
        }

        composable(
            route = Screen.RepositoryDetail.route,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            RepositoryDetailScreen(
                owner = owner,
                repo = repo,
                navController = navController
            )
        }
    }
}