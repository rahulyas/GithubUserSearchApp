package com.rahul.githubusersearchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rahul.githubusersearchapp.data.api.GitHubApiService
import com.rahul.githubusersearchapp.data.model.Repository
import com.rahul.githubusersearchapp.data.model.User
import com.rahul.githubusersearchapp.data.paging.RepositoryPagingSource
import com.rahul.githubusersearchapp.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.rahul.githubusersearchapp.domain.model.Result


@Singleton
class GitHubRepositoryImpl @Inject constructor(
    private val gitHubApiService: GitHubApiService
): GitHubRepository {

    override suspend fun getUser(username: String): Result<User> {
        return try {
            val user = gitHubApiService.getUser(username)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun searchUsers(query: String): Result<List<User>> {
        return try {
            val response = gitHubApiService.searchUsers(query, perPage = 10)
            Result.Success(response.items)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getUserRepositories(username: String, searchQuery: String): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RepositoryPagingSource(gitHubApiService, username, searchQuery)
            }
        ).flow
    }

    override suspend fun getPopularRepositories(username: String): Result<List<Repository>> {
        return try {
            val repositories = gitHubApiService.getUserRepositories(
                username = username,
                page = 1,
                perPage = 30,
                sort = "updated",
                direction = "desc"
            )
            // Sort by stars and take top 5
            val popularRepos = repositories
                .filter { !it.fork } // Exclude forked repositories for popular list
                .sortedByDescending { it.stargazers_count }
                .take(5)
            Result.Success(popularRepos)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getRepository(owner: String, repo: String): Result<Repository> {
        return try {
            val repository = gitHubApiService.getRepository(owner, repo)
            Result.Success(repository)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

