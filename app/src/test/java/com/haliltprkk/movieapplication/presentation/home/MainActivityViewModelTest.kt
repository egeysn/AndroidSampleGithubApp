package com.egeysn.githubapp.presentation.home

import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.domain.use_cases.home.GetUsersUseCase
import com.egeysn.githubapp.utils.MockHelper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
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
class MainActivityViewModelTest {

    private val page: Int = 1
    private val errorMessage: String = "error"

    @Mock
    private lateinit var getUsersUseCase: GetUsersUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = HomeViewModel(getUsersUseCase)
    }

    @Test
    fun `getPopularMoviesUseCase emits success`() = runTest {
        whenever(getUsersUseCase.getPopularMovies(any())).thenAnswer {
            flow { emit(Resource.Success(data = MockHelper.movieList)) }
        }
        viewModel.getMovies(page)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(HomeViewModel.HomeViewState.Success::class.java)
    }

    @Test
    fun `searchMovieUseCase emits success with empty list`() = runTest {
        whenever(getUsersUseCase.getPopularMovies(any())).thenAnswer {
            flow { emit(Resource.Success(data = arrayListOf<User>())) }
        }
        viewModel.getMovies(page)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(HomeViewModel.HomeViewState.SuccessWithEmptyData::class.java)
    }

    @Test
    fun `getPopularMoviesUseCase emits error`() = runTest {
        whenever(getUsersUseCase.getPopularMovies(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Error(message = UiText.DynamicString(value = errorMessage))) }
        }
        viewModel.getMovies(page)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(HomeViewModel.HomeViewState.Error::class.java)
    }

    @Test
    fun `getPopularMoviesUseCase emits loading`() = runTest {
        whenever(getUsersUseCase.getPopularMovies(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Loading()) }
        }
        viewModel.getMovies(page)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(HomeViewModel.HomeViewState.Loading::class.java)
    }

    @Test
    fun `verify getPopularMoviesUseCase called with correct parameter`() = runTest {
        whenever(getUsersUseCase.getPopularMovies(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Loading()) }
        }
        viewModel.getMovies(page)
        verify(getUsersUseCase).getPopularMovies(eq(page))
    }

    @Test
    fun `verify setLoading function called with isLoading=true `() = runTest {
        viewModel.setLoading(true)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(HomeViewModel.HomeViewState.Loading::class.java)
        val loadingState = currentState.value as HomeViewModel.HomeViewState.Loading
        assertThat(loadingState.isLoading).isEqualTo(true)
    }

    @Test
    fun `verify setLoading function called with isLoading=false `() = runTest {
        viewModel.setLoading(false)
        val currentState = viewModel.getViewState()
        assertThat(currentState.value).isInstanceOf(HomeViewModel.HomeViewState.Loading::class.java)
        val loadingState = currentState.value as HomeViewModel.HomeViewState.Loading
        assertThat(loadingState.isLoading).isEqualTo(false)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()
}
