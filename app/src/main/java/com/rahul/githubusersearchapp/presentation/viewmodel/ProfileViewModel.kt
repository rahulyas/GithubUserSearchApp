package com.rahul.githubusersearchapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.githubusersearchapp.domain.usecase.GetPopularRepositoriesUseCase
import com.rahul.githubusersearchapp.domain.usecase.GetUserUseCase
import com.rahul.githubusersearchapp.presentation.state.PopularReposState
import com.rahul.githubusersearchapp.presentation.state.RepositoriesState
import com.rahul.githubusersearchapp.presentation.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.rahul.githubusersearchapp.domain.model.Result
import com.rahul.githubusersearchapp.extensions.getUserFriendlyMessage


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getPopularRepositoriesUseCase: GetPopularRepositoriesUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _repositoriesState = MutableStateFlow<RepositoriesState>(RepositoriesState.Idle)
    val repositoriesState: StateFlow<RepositoriesState> = _repositoriesState.asStateFlow()

    private val _popularReposState = MutableStateFlow<PopularReposState>(PopularReposState.Idle)
    val popularReposState: StateFlow<PopularReposState> = _popularReposState.asStateFlow()

    fun loadUserProfile(username: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            when (val userResult = getUserUseCase(username)) {
                is Result.Success -> {
                    _userState.value = UserState.Success(userResult.data)
                    loadPopularRepositories(username)
                }
                is Result.Error -> {
                    _userState.value = UserState.Error(
                        userResult.exception.getUserFriendlyMessage()
                    )
                }
                Result.Loading -> TODO()
            }
        }
    }

    private suspend fun loadPopularRepositories(username: String) {
        _popularReposState.value = PopularReposState.Loading
        when (val result = getPopularRepositoriesUseCase(username)) {
            is Result.Success -> {
                _popularReposState.value = PopularReposState.Success(result.data)
            }
            is Result.Error -> {
                _popularReposState.value = PopularReposState.Error(
                    result.exception.getUserFriendlyMessage()
                )
            }
            Result.Loading -> TODO()
        }
    }
}
