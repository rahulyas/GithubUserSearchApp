package com.rahul.githubusersearchapp.domain.usecase

import androidx.paging.PagingData
import com.rahul.githubusersearchapp.data.model.Repository
import com.rahul.githubusersearchapp.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserRepositoriesUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    operator fun invoke(username: String, searchQuery: String): Flow<PagingData<Repository>> {
        return repository.getUserRepositories(username, searchQuery)
    }
}