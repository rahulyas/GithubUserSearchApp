package com.rahul.githubusersearchapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.githubusersearchapp.domain.usecase.GetUserUseCase
import com.rahul.githubusersearchapp.presentation.state.SearchState
import com.rahul.githubusersearchapp.presentation.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.rahul.githubusersearchapp.domain.model.Result
import com.rahul.githubusersearchapp.domain.usecase.SearchUsersUseCase
import com.rahul.githubusersearchapp.extensions.getUserFriendlyMessage
import com.rahul.githubusersearchapp.presentation.state.SimilarUsersState
import kotlinx.coroutines.delay

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _similarUsersState = MutableStateFlow<SimilarUsersState>(SimilarUsersState.Idle)
    val similarUsersState: StateFlow<SimilarUsersState> = _similarUsersState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchState.update { it.copy(searchText = text) }
    }

    fun searchUser() {
        val username = _searchState.value.searchText.trim()
        if (username.isBlank()) return

        viewModelScope.launch {
            _userState.value = UserState.Loading
            when (val result = getUserUseCase(username)) {
                is Result.Success -> {
                    _userState.value = UserState.Success(result.data)
                    // Also search for similar users
                    searchSimilarUsers(username)
                }
                is Result.Error -> {
                    _userState.value = UserState.Error(
                        result.exception.getUserFriendlyMessage()
                    )
                }
                Result.Loading -> TODO()
            }
        }
    }

    private suspend fun searchSimilarUsers(query: String) {
        when (val result = searchUsersUseCase(query)) {
            is Result.Success -> {
                // Filter out the exact match if it exists
                val filteredUsers = result.data.filter {
                    it.login.lowercase() != query.lowercase()
                }.take(5)
                _similarUsersState.value = SimilarUsersState.Success(filteredUsers)
            }
            is Result.Error -> {
                _similarUsersState.value = SimilarUsersState.Error(
                    result.exception.getUserFriendlyMessage()
                )
            }
            Result.Loading -> {
                _similarUsersState.value = SimilarUsersState.Loading
            }
        }
    }

    fun refreshData() {
        val currentUsername = when (val state = _userState.value) {
            is UserState.Success -> state.user.login
            else -> _searchState.value.searchText.trim()
        }

        if (currentUsername.isNotBlank()) {
            viewModelScope.launch {
                _isRefreshing.value = true
                delay(500) // Minimum refresh duration for better UX

                when (val result = getUserUseCase(currentUsername)) {
                    is Result.Success -> {
                        _userState.value = UserState.Success(result.data)
                        searchSimilarUsers(currentUsername)
                    }
                    is Result.Error -> {
                        _userState.value = UserState.Error(
                            result.exception.getUserFriendlyMessage()
                        )
                    }
                    Result.Loading -> TODO()
                }

                _isRefreshing.value = false
            }
        } else {
            // Just refresh similar users if we have search text
            val searchText = _searchState.value.searchText.trim()
            if (searchText.isNotBlank()) {
                viewModelScope.launch {
                    _isRefreshing.value = true
                    searchSimilarUsers(searchText)
                    delay(500)
                    _isRefreshing.value = false
                }
            }
        }
    }

    fun resetUserState() {
        _userState.value = UserState.Idle
        _similarUsersState.value = SimilarUsersState.Idle
    }
}