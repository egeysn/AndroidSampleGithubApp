package com.egeysn.githubapp.data.repositories

import com.egeysn.githubapp.data.local.UserDao
import com.egeysn.githubapp.data.local.entities.UserEntity
import com.egeysn.githubapp.data.remote.api_services.GithubApiService
import com.egeysn.githubapp.data.remote.models.UserDto
import com.egeysn.githubapp.data.remote.models.response.UsersResponse
import com.egeysn.githubapp.domain.mappers.UserMapper
import com.egeysn.githubapp.domain.repositories.GithubRepository
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val api: GithubApiService,
    private val dao: UserDao,
    private val mapper: UserMapper
) :
    GithubRepository {
    override suspend fun getUsers(): UsersResponse = api.getPopularMovies()
    override suspend fun getCachedPopularMovies(page: Int): List<UserEntity> = dao.getUsers()
    override suspend fun getUsersFromDatabase(page: Int): List<UserEntity> = dao.getUsers()
    override suspend fun saveUsersToDataBase(response: List<UserDto>) =
        dao.insertUsers(response.map { mapper.fromDtoToEntity(it) })

    override suspend fun deleteUsersFromDatabase() = dao.deleteAllUsers()
    override suspend fun getUser(userName: String): UserDto = api.getUser(userName)
    override suspend fun searchUser(query: String, perPage: Int): UsersResponse = api.searchUsers(query, perPage)
}
