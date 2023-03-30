package com.egeysn.githubapp.data.remote.api_services

import com.egeysn.githubapp.data.remote.models.UserDto
import com.egeysn.githubapp.data.remote.models.response.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("users")
    suspend fun getPopularMovies(): UsersResponse

    @GET("users/{username}")
    suspend fun getUser(@Path("username") userName: String): UserDto

    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String, @Query("per_page") perPage: Int): UsersResponse
}
