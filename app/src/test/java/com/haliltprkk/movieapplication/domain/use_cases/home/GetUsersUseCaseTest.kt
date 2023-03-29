package com.egeysn.githubapp.domain.use_cases.home

import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.data.services.localStorage.LocalStorageService
import com.egeysn.githubapp.domain.mappers.UserMapper
import com.egeysn.githubapp.domain.repositories.GithubRepository
import com.egeysn.githubapp.utils.MockHelper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetUsersUseCaseTest {
    private lateinit var getUsersUseCase: GetUsersUseCase

    @Mock
    private lateinit var githubRepository: GithubRepository

    @Mock
    private lateinit var localeStorageService: LocalStorageService

    @Mock
    private lateinit var mapper: UserMapper
    private val page: Int = 0

    @Before
    fun setUp() {
        getUsersUseCase = GetUsersUseCaseImpl(githubRepository, localeStorageService, mapper)
    }

    @Test
    fun `check getPopularMovies() success case`() = runBlocking {
        // when
        whenever(githubRepository.getUsers(page)).thenAnswer { MockHelper.usersResponse }
        val result = getUsersUseCase.getPopularMovies(page)
        val flowList = result.toList()
        // then
        assertThat(flowList[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(flowList[1]).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun `check getPopularMovies() http exception error case`() = runBlocking {
        // when
        whenever(githubRepository.getUsers(page)).thenAnswer { throw MockHelper.getHttpException() }
        val result = getUsersUseCase.getPopularMovies(page)
        val flowList = result.toList()
        // then
        assertThat(flowList[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(flowList[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(flowList[1].message).isInstanceOf(UiText.DynamicString::class.java)
    }

    @Test
    fun `check getPopularMovies() io exception error case`() = runBlocking {
        // when
        whenever(githubRepository.getUsers(page)).thenAnswer { throw MockHelper.ioException }
        val result = getUsersUseCase.getPopularMovies(page)
        val flowList = result.toList()
        // then

        assertThat(flowList[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(flowList[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(flowList[1].message).isInstanceOf(UiText.StringResource::class.java)
    }
}
