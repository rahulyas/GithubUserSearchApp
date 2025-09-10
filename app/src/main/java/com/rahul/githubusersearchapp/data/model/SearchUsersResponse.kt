package com.rahul.githubusersearchapp.data.model

data class SearchUsersResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<User>
)