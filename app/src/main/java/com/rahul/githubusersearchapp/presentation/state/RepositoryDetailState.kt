package com.rahul.githubusersearchapp.presentation.state

import com.rahul.githubusersearchapp.data.model.Repository

sealed class RepositoryDetailState {
    data object Idle : RepositoryDetailState()
    data object Loading : RepositoryDetailState()
    data class Success(val repository: Repository) : RepositoryDetailState()
    data class Error(val message: String) : RepositoryDetailState()
}