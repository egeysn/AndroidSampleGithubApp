package com.egeysn.githubapp.data.repositories

import com.egeysn.githubapp.data.local.UserDao
import com.egeysn.githubapp.data.local.entities.MovieEntity
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
    override suspend fun getCachedPopularMovies(page: Int): List<MovieEntity> = dao.getPopularMovies()
    override suspend fun getUsersFromDatabase(page: Int): List<MovieEntity> = dao.getPopularMovies()
    override suspend fun saveUsersToDataBase(response: List<UserDto>) =
        dao.insertMovies(response.map { mapper.fromDtoToEntity(it) })

    override suspend fun deleteUsersFromDatabase() = dao.deleteAllMovies()
    override suspend fun getUser(id: Long): UserDto = api.getMovie(id)
    override suspend fun searchUser(query: String, perPage: Int): UsersResponse = api.searchUsers(query, perPage)
}
