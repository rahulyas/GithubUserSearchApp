package com.rahul.githubusersearchapp.presentation.state

import com.rahul.githubusersearchapp.data.model.User

sealed class SimilarUsersState {
    data object Idle : SimilarUsersState()
    data object Loading : SimilarUsersState()
    data class Success(val users: List<User>) : SimilarUsersState()
    data class Error(val message: String) : SimilarUsersState()
}