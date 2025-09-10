package com.rahul.githubusersearchapp.presentation.state

import com.rahul.githubusersearchapp.data.model.User

sealed class UserState {
    data object Idle : UserState()
    data object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}