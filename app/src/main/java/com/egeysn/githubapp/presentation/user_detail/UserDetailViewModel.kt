package com.egeysn.githubapp.presentation.user_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egeysn.githubapp.R
import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.domain.use_cases.user_detail.GetUserDetailUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUserCase: GetUserDetailUserCase,
) : ViewModel() {
    private val _state = MutableStateFlow<MovieDetailViewState>(MovieDetailViewState.Init)
    fun getViewState(): StateFlow<MovieDetailViewState> = _state.asStateFlow()

    fun setLoading(isLoading: Boolean) {
        _state.value = MovieDetailViewState.Loading(isLoading)
    }

    fun getMovie(username: String) {
        viewModelScope.launch {
            getUserDetailUserCase.getMovieByUserName(username).onEach {
                when (it) {
                    is Resource.Error -> {
                        setLoading(false)
                        _state.value = MovieDetailViewState.Error(it.message)
                    }
                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        setLoading(false)
                        if (it.data == null) {
                            _state.value = MovieDetailViewState.Error(
                                UiText.StringResource(R.string.userDetailPage_emptyError)
                            )
                        } else {
                            _state.value = MovieDetailViewState.Success(data = it.data)
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class MovieDetailViewState {
        object Init : MovieDetailViewState()
        data class Loading(val isLoading: Boolean) : MovieDetailViewState()
        data class Success(val data: User) : MovieDetailViewState()
        data class Error(val error: UiText) : MovieDetailViewState()
    }
}
