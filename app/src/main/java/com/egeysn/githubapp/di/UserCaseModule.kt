package com.egeysn.githubapp.di

import com.egeysn.githubapp.domain.use_cases.home.GetUsersUseCase
import com.egeysn.githubapp.domain.use_cases.home.GetUsersUseCaseImpl
import com.egeysn.githubapp.domain.use_cases.search.SearchUserUseCase
import com.egeysn.githubapp.domain.use_cases.search.SearchUserUseCaseImpl
import com.egeysn.githubapp.domain.use_cases.user_detail.GetUserDetailUserCase
import com.egeysn.githubapp.domain.use_cases.user_detail.GetUserDetailUserCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserCaseModule {
    @Binds
    @Singleton
    abstract fun bindGetPopularMoviesUseCase(
        getPopularMoviesUseCaseImpl: GetUsersUseCaseImpl
    ): GetUsersUseCase

    @Binds
    @Singleton
    abstract fun bindGetMovieDetailUseCase(
        getMovieDetailUseCaseImpl: GetUserDetailUserCaseImpl
    ): GetUserDetailUserCase

    @Binds
    @Singleton
    abstract fun bindSearchMovieUseCase(
        searchUserUseCaseImpl: SearchUserUseCaseImpl
    ): SearchUserUseCase
}
