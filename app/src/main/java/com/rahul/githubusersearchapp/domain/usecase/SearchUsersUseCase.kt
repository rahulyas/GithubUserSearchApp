package com.rahul.githubusersearchapp.domain.usecase

import com.rahul.githubusersearchapp.data.model.User
import com.rahul.githubusersearchapp.domain.repository.GitHubRepository
import javax.inject.Inject
import com.rahul.githubusersearchapp.domain.model.Result

class SearchUsersUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(query: String): Result<List<User>> {
        return repository.searchUsers(query)
    }
}