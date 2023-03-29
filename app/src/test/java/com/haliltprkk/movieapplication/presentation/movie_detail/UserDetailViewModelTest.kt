package com.egeysn.githubapp.presentation.movie_detail

import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.domain.use_cases.user_detail.GetUserDetailUserCase
import com.egeysn.githubapp.utils.MockHelper
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
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
class UserDetailViewModelTest {

    private val movieId: Long = 1
    private val errorMessage: String = "error"

    @Mock
    private lateinit var getUserDetailUserCase: GetUserDetailUserCase

    private lateinit var viewModel: UserDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = UserDetailViewModel(getUserDetailUserCase)
    }

    @Test
    fun `getMovieDetailUseCase emits success`() = runTest {
        whenever(getUserDetailUserCase.getMovieById(any())).thenAnswer {
            flow { emit(Resource.Success(data = MockHelper.movie)) }
        }
        viewModel.getMovie(movieId)
        val currentState = viewModel.getViewState()
        Truth.assertThat(currentState.value).isInstanceOf(UserDetailViewModel.MovieDetailViewState.Success::class.java)
    }

    @Test
    fun `getMovieDetailUseCase emits error`() = runTest {
        whenever(getUserDetailUserCase.getMovieById(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Error(message = UiText.DynamicString(value = errorMessage))) }
        }
        viewModel.getMovie(movieId)
        val currentState = viewModel.getViewState()
        Truth.assertThat(currentState.value).isInstanceOf(UserDetailViewModel.MovieDetailViewState.Error::class.java)
    }

    @Test
    fun `getMovieDetailUseCase emits loading`() = runTest {
        whenever(getUserDetailUserCase.getMovieById(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Loading()) }
        }
        viewModel.getMovie(movieId)
        val currentState = viewModel.getViewState()
        Truth.assertThat(currentState.value).isInstanceOf(UserDetailViewModel.MovieDetailViewState.Loading::class.java)
    }

    @Test
    fun `verify getMovieDetailUseCase called with correct parameter`() = runTest {
        whenever(getUserDetailUserCase.getMovieById(any())).thenAnswer {
            flow<Resource<Any>> { emit(Resource.Loading()) }
        }
        viewModel.getMovie(movieId)
        verify(getUserDetailUserCase).getMovieById(eq(movieId))
    }

    @Test
    fun `verify setLoading function called with isLoading=true `() = runTest {
        viewModel.setLoading(true)
        val currentState = viewModel.getViewState()
        Truth.assertThat(currentState.value).isInstanceOf(UserDetailViewModel.MovieDetailViewState.Loading::class.java)
        val loadingState = currentState.value as UserDetailViewModel.MovieDetailViewState.Loading
        Truth.assertThat(loadingState.isLoading).isEqualTo(true)
    }

    @Test
    fun `verify setLoading function called with isLoading=false `() = runTest {
        viewModel.setLoading(false)
        val currentState = viewModel.getViewState()
        Truth.assertThat(currentState.value).isInstanceOf(UserDetailViewModel.MovieDetailViewState.Loading::class.java)
        val loadingState = currentState.value as UserDetailViewModel.MovieDetailViewState.Loading
        Truth.assertThat(loadingState.isLoading).isEqualTo(false)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()
}
