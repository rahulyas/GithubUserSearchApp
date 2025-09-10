package com.rahul.githubusersearchapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.githubusersearchapp.domain.usecase.GetRepositoryUseCase
import com.rahul.githubusersearchapp.presentation.state.RepositoryDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.rahul.githubusersearchapp.domain.model.Result
import com.rahul.githubusersearchapp.extensions.getUserFriendlyMessage

@HiltViewModel
class RepositoryDetailViewModel @Inject constructor(
    private val getRepositoryUseCase: GetRepositoryUseCase
) : ViewModel() {

    private val _repositoryState = MutableStateFlow<RepositoryDetailState>(RepositoryDetailState.Idle)
    val repositoryState: StateFlow<RepositoryDetailState> = _repositoryState.asStateFlow()

    fun loadRepository(owner: String, repo: String) {
        viewModelScope.launch {
            _repositoryState.value = RepositoryDetailState.Loading
            when (val result = getRepositoryUseCase(owner, repo)) {
                is Result.Success -> {
                    _repositoryState.value = RepositoryDetailState.Success(result.data)
                }
                is Result.Error -> {
                    _repositoryState.value = RepositoryDetailState.Error(
                        result.exception.getUserFriendlyMessage()
                    )
                }
                Result.Loading -> TODO()
            }
        }
    }
}
