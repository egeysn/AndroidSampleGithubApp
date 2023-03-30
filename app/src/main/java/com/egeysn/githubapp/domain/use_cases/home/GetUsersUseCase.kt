package com.egeysn.githubapp.domain.use_cases.home

import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.domain.models.User
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCase {
    suspend fun getUsers(page: Int): Flow<Resource<List<User>>>
}
