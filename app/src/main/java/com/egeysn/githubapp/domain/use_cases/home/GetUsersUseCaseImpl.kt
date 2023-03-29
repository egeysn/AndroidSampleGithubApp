package com.egeysn.githubapp.domain.use_cases.home

import com.egeysn.githubapp.R
import com.egeysn.githubapp.common.extension.handleError
import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.data.local.entities.MovieEntity
import com.egeysn.githubapp.data.services.localStorage.LocalStorageService
import com.egeysn.githubapp.domain.mappers.UserMapper
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.domain.repositories.GithubRepository
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetUsersUseCaseImpl @Inject constructor(
    private val repository: GithubRepository,
    private val localStorageService: LocalStorageService,
    private val mapper: UserMapper
) :
    GetUsersUseCase {
    override suspend fun getPopularMovies(page: Int): Flow<Resource<List<User>>> = flow {
        try {
            emit(Resource.Loading())
            val currentTime = System.currentTimeMillis()
            val lastUpdateTime = localStorageService.getLastUpdateTime() ?: 0
            if (currentTime - lastUpdateTime < CACHE_EXPIRATION_TIME) {
                val cachedMovies: List<MovieEntity> = repository.getCachedPopularMovies(page)
                if (cachedMovies.isNotEmpty()) {
                    emit(Resource.Success(data = cachedMovies.map(mapper::fromEntityToDomain)))
                    return@flow
                }
            }
            val response = repository.getUsers()
            repository.deleteUsersFromDatabase()
            repository.saveUsersToDataBase(response)
            localStorageService.setLastUpdateTime(System.currentTimeMillis())
            emit(Resource.Success(data = response.map(mapper::fromDtoToDomain)))
        } catch (e: HttpException) {
            emit(Resource.Error(e.handleError()))
        } catch (e: IOException) {
            emit(Resource.Error(UiText.StringResource(R.string.couldntReachServerError)))
        }
    }

    companion object {
        val CACHE_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(5)
    }
}
