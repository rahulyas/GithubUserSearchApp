package com.rahul.githubusersearchapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rahul.githubusersearchapp.data.model.Repository
import com.rahul.githubusersearchapp.domain.usecase.GetUserRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val getUserRepositoriesUseCase: GetUserRepositoriesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _username = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val repositories: Flow<PagingData<Repository>> = combine(
        _username,
        _searchQuery.debounce(300)
    ) { username, query ->
        Pair(username, query)
    }.flatMapLatest { (username, query) ->
        if (username.isBlank()) {
            flowOf(PagingData.empty())
        } else {
            getUserRepositoriesUseCase(username, query)
        }
    }.cachedIn(viewModelScope)

    fun setUsername(username: String) {
        _username.value = username
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}