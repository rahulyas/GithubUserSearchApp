package com.rahul.githubusersearchapp.presentation.navigation

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object Profile : Screen("profile/{username}") {
        fun createRoute(username: String) = "profile/$username"
    }
    data object Repositories : Screen("repositories/{username}") {
        fun createRoute(username: String) = "repositories/$username"
    }
    data object RepositoryDetail : Screen("repository/{owner}/{repo}") {
        fun createRoute(owner: String, repo: String) = "repository/$owner/$repo"
    }
}