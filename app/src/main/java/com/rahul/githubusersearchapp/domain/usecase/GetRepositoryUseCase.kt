package com.rahul.githubusersearchapp.domain.usecase

import com.rahul.githubusersearchapp.data.model.Repository
import com.rahul.githubusersearchapp.domain.model.Result
import com.rahul.githubusersearchapp.domain.repository.GitHubRepository
import javax.inject.Inject

class GetRepositoryUseCase @Inject constructor(private val repository: GitHubRepository) {
    suspend operator fun invoke(owner: String, repo: String): Result<Repository> = repository.getRepository(owner, repo)
}