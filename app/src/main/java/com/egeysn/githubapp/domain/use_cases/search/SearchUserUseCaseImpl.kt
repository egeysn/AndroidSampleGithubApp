package com.egeysn.githubapp.domain.use_cases.search

import com.egeysn.githubapp.common.utils.performRequest
import com.egeysn.githubapp.domain.mappers.UserMapper
import com.egeysn.githubapp.domain.repositories.GithubRepository
import javax.inject.Inject

class SearchUserUseCaseImpl @Inject constructor(
    private val repository: GithubRepository,
    private val mapper: UserMapper
) : SearchUserUseCase {
    override suspend fun searchUser(query: String) =
        performRequest(
            mapper = { response -> response.items?.map(mapper::fromDtoToDomain) },
            networkCall = { repository.searchUser(query, 30) },
        )
}
