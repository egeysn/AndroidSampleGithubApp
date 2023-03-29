package com.egeysn.githubapp.domain.use_cases.search

import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
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
class SearchUserUseCaseTest {
    private lateinit var searchUserUseCase: SearchUserUseCase

    @Mock
    private lateinit var githubRepository: GithubRepository

    @Mock
    private lateinit var mapper: UserMapper
    private val query: String = "query"

    @Before
    fun setUp() {
        searchUserUseCase = SearchUserUseCaseImpl(githubRepository, mapper)
    }

    @Test
    fun `check searchMovie() success case`() = runBlocking {
        // when
        whenever(githubRepository.searchUser(query)).thenAnswer { MockHelper.searchMovieDt }
        val result = searchUserUseCase.searchUser(query)
        val flowList = result.toList()
        // then
        assertThat(flowList[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(flowList[1]).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun `check searchMovie() http exception error case`() = runBlocking {
        // when
        whenever(githubRepository.searchUser(query)).thenAnswer { throw MockHelper.getHttpException() }
        val result = searchUserUseCase.searchUser(query)
        val flowList = result.toList()
        // then
        assertThat(flowList[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(flowList[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(flowList[1].message).isInstanceOf(UiText.DynamicString::class.java)
    }

    @Test
    fun `check searchMovie() io exception error case`() = runBlocking {
        // when
        whenever(githubRepository.searchUser(query)).thenAnswer { throw MockHelper.ioException }
        val result = searchUserUseCase.searchUser(query)
        val flowList = result.toList()
        // then
        assertThat(flowList[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(flowList[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(flowList[1].message).isInstanceOf(UiText.StringResource::class.java)
    }
}
