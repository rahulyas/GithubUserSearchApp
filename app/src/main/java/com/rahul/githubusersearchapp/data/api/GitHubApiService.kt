package com.rahul.githubusersearchapp.data.api

import com.rahul.githubusersearchapp.data.model.Repository
import com.rahul.githubusersearchapp.data.model.SearchUsersResponse
import com.rahul.githubusersearchapp.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): User

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 30
    ): SearchUsersResponse

    @GET("users/{username}/repos")
    suspend fun getUserRepositories(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30,
        @Query("sort") sort: String = "updated",
        @Query("direction") direction: String = "desc"
    ): List<Repository>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Repository
}
