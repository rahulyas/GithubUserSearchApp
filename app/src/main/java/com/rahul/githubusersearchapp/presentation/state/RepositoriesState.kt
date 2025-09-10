package com.rahul.githubusersearchapp.presentation.state

import com.rahul.githubusersearchapp.data.model.Repository

sealed class RepositoriesState {
    data object Idle : RepositoriesState()
    data object Loading : RepositoriesState()
    data class Success(val repositories: List<Repository>) : RepositoriesState()
    data class Error(val message: String) : RepositoriesState()
}