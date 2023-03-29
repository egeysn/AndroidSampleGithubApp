package com.egeysn.githubapp.domain.use_cases.search

import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.domain.models.User
import kotlinx.coroutines.flow.Flow

interface SearchUserUseCase {
    suspend fun searchUser(query: String): Flow<Resource<List<User>>>
}
