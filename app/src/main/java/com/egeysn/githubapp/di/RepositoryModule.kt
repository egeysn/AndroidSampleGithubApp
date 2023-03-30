package com.egeysn.githubapp.di

import com.egeysn.githubapp.data.repositories.GithubRepositoryImpl
import com.egeysn.githubapp.domain.repositories.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGithubRepository(
        movieRepositoryImpl: GithubRepositoryImpl
    ): GithubRepository
}
