package com.egeysn.githubapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egeysn.githubapp.R
import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.domain.use_cases.search.SearchUserUseCase
import com.egeysn.githubapp.presentation.favoriteManager.FavoriteManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUserUseCase: SearchUserUseCase,
    private val favoriteManager: FavoriteManager
) : ViewModel() {
    private val _state = MutableStateFlow<SearchViewState>(SearchViewState.Init)
    fun getViewState(): StateFlow<SearchViewState> = _state.asStateFlow()
    private var searchJob: Job? = null

    fun setLoading(isLoading: Boolean) {
        _state.value = SearchViewState.Loading(isLoading)
    }

    fun getFavoriteState(): StateFlow<List<Int>> = favoriteManager.favoriteList
    fun saveFavorite(id: Int) {
        viewModelScope.launch {
            favoriteManager.saveFavorite(id)
        }
    }

    fun deleteFavorite(id: Int) {
        viewModelScope.launch {
            favoriteManager.deleteFavorite(id)
        }
    }

    fun searchUser(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            searchUserUseCase.searchUser(query).onEach {
                when (it) {
                    is Resource.Error -> {
                        setLoading(false)
                        _state.value = SearchViewState.Error(it.message)
                    }
                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        setLoading(false)
                        if (!it.data.isNullOrEmpty()) {
                            _state.value = SearchViewState.Success(it.data)
                        } else {
                            _state.value = SearchViewState.Error(
                                UiText.StringResource(R.string.searchPage_noUserText)
                            )
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class SearchViewState {
        object Init : SearchViewState()
        data class Loading(val isLoading: Boolean) : SearchViewState()
        data class Success(val data: List<User>) : SearchViewState()
        data class Error(val error: UiText) : SearchViewState()
    }
}
