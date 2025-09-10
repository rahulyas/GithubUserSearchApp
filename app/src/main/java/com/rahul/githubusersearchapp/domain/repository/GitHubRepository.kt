package com.rahul.githubusersearchapp.domain.repository

import androidx.paging.PagingData
import com.rahul.githubusersearchapp.data.model.Repository
import com.rahul.githubusersearchapp.data.model.User
import kotlinx.coroutines.flow.Flow
import com.rahul.githubusersearchapp.domain.model.Result

interface GitHubRepository {
    suspend fun getUser(username: String): Result<User>
    suspend fun searchUsers(query: String): Result<List<User>>
    fun getUserRepositories(username: String, searchQuery: String): Flow<PagingData<Repository>>
    suspend fun getPopularRepositories(username: String): Result<List<Repository>>
    suspend fun getRepository(owner: String, repo: String): Result<Repository>
}