package com.egeysn.githubapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.domain.use_cases.home.GetUsersUseCase
import com.egeysn.githubapp.presentation.favoriteManager.FavoriteManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val favoriteManager: FavoriteManager
) : ViewModel() {
    private val _state = MutableStateFlow<HomeViewState>(HomeViewState.Init)
    fun getViewState(): StateFlow<HomeViewState> = _state.asStateFlow()

    fun getFavoriteState(): StateFlow<List<Int>> = favoriteManager.favoriteList

    fun setLoading(isLoading: Boolean) {
        _state.value = HomeViewState.Loading(isLoading)
    }

    fun saveFavorite(id: Int) {
        favoriteManager.saveFavorite(id)
    }

    fun deleteFavorite(id: Int) {
        favoriteManager.deleteFavorite(id)
    }

    fun getUsers(page: Int) {
        viewModelScope.launch {
            getUsersUseCase.getUsers(page).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        setLoading(false)
                        _state.value = HomeViewState.Error(result.message)
                    }
                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        setLoading(false)
                        if (result.data == null || result.data.isEmpty()) {
                            _state.value = HomeViewState.SuccessWithEmptyData
                        } else {
                            _state.value = HomeViewState.Success(result.data)
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class HomeViewState {
        object Init : HomeViewState()
        data class Loading(val isLoading: Boolean) : HomeViewState()
        data class Success(val data: List<User>) : HomeViewState()
        object SuccessWithEmptyData : HomeViewState()
        data class Error(val error: UiText) : HomeViewState()
    }
}
