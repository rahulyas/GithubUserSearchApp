package com.rahul.githubusersearchapp.di

import com.rahul.githubusersearchapp.data.api.ApiClient
import com.rahul.githubusersearchapp.data.api.GitHubApiService
import com.rahul.githubusersearchapp.data.repository.GitHubRepositoryImpl
import com.rahul.githubusersearchapp.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGitHubApiService(): GitHubApiService {
        return ApiClient.gitHubApiService
    }

    @Provides
    @Singleton
    fun provideGitHubRepository(apiService: GitHubApiService): GitHubRepository {
        return GitHubRepositoryImpl(apiService)
    }

}