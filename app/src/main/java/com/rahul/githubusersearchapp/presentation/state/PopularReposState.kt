package com.rahul.githubusersearchapp.presentation.state

import com.rahul.githubusersearchapp.data.model.Repository

sealed class PopularReposState {
    data object Idle : PopularReposState()
    data object Loading : PopularReposState()
    data class Success(val repositories: List<Repository>) : PopularReposState()
    data class Error(val message: String) : PopularReposState()
}