package com.egeysn.githubapp.domain.repositories

import com.egeysn.githubapp.data.local.entities.UserEntity
import com.egeysn.githubapp.data.remote.models.UserDto
import com.egeysn.githubapp.data.remote.models.response.UsersResponse

interface GithubRepository {
    suspend fun getUsers(): UsersResponse

    suspend fun getCachedPopularMovies(page: Int): List<UserEntity>

    suspend fun getUser(userName: String): UserDto

    suspend fun searchUser(query: String, perPage: Int): UsersResponse

    suspend fun getUsersFromDatabase(page: Int): List<UserEntity>
    suspend fun saveUsersToDataBase(response: List<UserDto>)
    suspend fun deleteUsersFromDatabase()
}
