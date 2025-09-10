package com.rahul.githubusersearchapp.domain.usecase

import com.rahul.githubusersearchapp.data.model.User
import com.rahul.githubusersearchapp.domain.repository.GitHubRepository
import javax.inject.Inject
import com.rahul.githubusersearchapp.domain.model.Result


class GetUserUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(username: String): Result<User> {
        return repository.getUser(username)
    }
}