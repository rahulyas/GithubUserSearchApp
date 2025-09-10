package com.rahul.githubusersearchapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.githubusersearchapp.data.api.GitHubApiService
import com.rahul.githubusersearchapp.data.model.Repository

class RepositoryPagingSource(
    private val apiService: GitHubApiService,
    private val username: String,
    private val searchQuery: String
) : PagingSource<Int, Repository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        return try {
            val page = params.key ?: 1
            val repositories = apiService.getUserRepositories(
                username = username,
                page = page,
                perPage = params.loadSize
            )

            // Filter repositories based on search query
            val filteredRepositories = if (searchQuery.isBlank()) {
                repositories
            } else {
                repositories.filter { repo ->
                    repo.name.contains(searchQuery, ignoreCase = true) ||
                            repo.description?.contains(searchQuery, ignoreCase = true) == true ||
                            repo.language?.contains(searchQuery, ignoreCase = true) == true
                }
            }

            LoadResult.Page(
                data = filteredRepositories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (repositories.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
