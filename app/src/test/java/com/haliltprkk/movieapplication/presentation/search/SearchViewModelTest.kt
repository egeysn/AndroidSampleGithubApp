package com.egeysn.githubapp.presentation.search

import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.domain.use_cases.search.SearchUserUseCase
import com.egeysn.githubapp.utils.MockHelper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    private val query: String = "wakanda"
    private val errorMessage: String = "error"

    @Mock
    private lateinit var searchUserUseCase: SearchUserUseCase

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = SearchViewModel(searchUserUseCase)
    }

    @Test
    fun `searchMovieUseCase emits success`() = runTest {
        whenever(searchUserUseCase.searchUser(any())).thenAnswer {
            flow { emit(Resource.Success(data = MockHelper.movieList)) }
        }
        viewModel.searchMovie(query)
        delay(500)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(SearchViewModel.SearchViewState.Success::class.java)
    }

    @Test
    fun `searchMovieUseCase emits success with empty list`() = runTest {
        whenever(searchUserUseCase.searchUser(any())).thenAnswer {
            flow { emit(Resource.Success(data = arrayListOf<User>())) }
        }
        viewModel.searchMovie(query)
        delay(500)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(SearchViewModel.SearchViewState.Error::class.java)
    }

    @Test
    fun `searchMovieUseCase emits error`() = runTest {
        whenever(searchUserUseCase.searchUser(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Error(message = UiText.DynamicString(value = errorMessage))) }
        }
        viewModel.searchMovie(query)
        delay(500)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(SearchViewModel.SearchViewState.Error::class.java)
    }

    @Test
    fun `searchMovieUseCase emits loading`() = runTest {
        whenever(searchUserUseCase.searchUser(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Loading()) }
        }
        viewModel.searchMovie(query)
        delay(500)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(SearchViewModel.SearchViewState.Loading::class.java)
    }

    @Test
    fun `verify searchMovieUseCase called with correct parameter`() = runTest {
        whenever(searchUserUseCase.searchUser(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Loading()) }
        }
        viewModel.searchMovie(query)
        delay(500)
        verify(searchUserUseCase).searchUser(eq(query))
    }

    @Test
    fun `verify setLoading function called with isLoading=true `() = runTest {
        viewModel.setLoading(true)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(SearchViewModel.SearchViewState.Loading::class.java)
        val loadingState = currentState.value as SearchViewModel.SearchViewState.Loading
        assertThat(loadingState.isLoading).isEqualTo(true)
    }

    @Test
    fun `verify setLoading function called with isLoading=false `() = runTest {
        viewModel.setLoading(false)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(SearchViewModel.SearchViewState.Loading::class.java)
        val loadingState = currentState.value as SearchViewModel.SearchViewState.Loading
        assertThat(loadingState.isLoading).isEqualTo(false)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()
}
