package com.egeysn.githubapp.di

import com.egeysn.githubapp.data.repositories.GithubRepositoryImpl
import com.egeysn.githubapp.presentation.favoriteManager.FavoriteManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ManagerModule {

    @Provides
    @Singleton
    fun bindFavoriteManager(
        githubRepositoryImpl: GithubRepositoryImpl
    ): FavoriteManager = FavoriteManager(githubRepositoryImpl)
}
