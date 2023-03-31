package com.egeysn.githubapp.data.repositories

import com.egeysn.githubapp.data.local.UserDao
import com.egeysn.githubapp.data.local.entities.FavEntity
import com.egeysn.githubapp.data.local.entities.UserEntity
import com.egeysn.githubapp.data.remote.api_services.GithubApiService
import com.egeysn.githubapp.data.remote.models.UserDto
import com.egeysn.githubapp.data.remote.models.response.SearchUsersResponse
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
    override suspend fun getUsers(): UsersResponse = api.getUsers()
    override suspend fun getCachedUsers(page: Int): List<UserEntity> = dao.getUsers()
    override suspend fun getUsersFromDatabase(page: Int): List<UserEntity> = dao.getUsers()
    override suspend fun saveUsersToDataBase(response: List<UserDto>) =
        dao.insertUsers(response.map { mapper.fromDtoToEntity(it) })

    override suspend fun deleteUsersFromDatabase() = dao.deleteAllUsers()
    override suspend fun insertFavorite(entity: FavEntity) { dao.insertFavorite(entity) }

    override suspend fun getFavorites(): List<FavEntity> = dao.getFavorites()

    override suspend fun deleteFavorite(id: Int) { dao.deleteFavorite(id) }

    override suspend fun getUser(userName: String): UserDto = api.getUser(userName)
    override suspend fun searchUser(query: String, perPage: Int): SearchUsersResponse = api.searchUsers(query, perPage)
}
