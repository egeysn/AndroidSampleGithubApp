package com.egeysn.githubapp.domain.use_cases.user_detail

import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.domain.models.User
import kotlinx.coroutines.flow.Flow

interface GetUserDetailUserCase {
    suspend fun getMovieByUserName(username: String): Flow<Resource<User>>
}
