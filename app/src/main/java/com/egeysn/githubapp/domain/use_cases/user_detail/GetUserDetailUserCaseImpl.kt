package com.egeysn.githubapp.domain.use_cases.user_detail

import com.egeysn.githubapp.common.utils.performRequest
import com.egeysn.githubapp.domain.mappers.UserMapper
import com.egeysn.githubapp.domain.repositories.GithubRepository
import javax.inject.Inject

class GetUserDetailUserCaseImpl @Inject constructor(
    private val repository: GithubRepository,
    private val mapper: UserMapper
) : GetUserDetailUserCase {
    override suspend fun getUserByUserName(username: String) =
        performRequest(
            mapper = mapper::fromDtoToDomain,
            networkCall = { repository.getUser(username) },
        )
}
