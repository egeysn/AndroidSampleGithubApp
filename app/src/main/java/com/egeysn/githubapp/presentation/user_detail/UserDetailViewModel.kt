package com.egeysn.githubapp.presentation.user_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egeysn.githubapp.R
import com.egeysn.githubapp.common.utils.Resource
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.domain.use_cases.user_detail.GetUserDetailUserCase
import com.egeysn.githubapp.presentation.favoriteManager.FavoriteManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUserCase: GetUserDetailUserCase,
    private val favoriteManager: FavoriteManager
) : ViewModel() {
    private val _state = MutableStateFlow<UserDetail>(UserDetail.Init)
    fun getViewState(): StateFlow<UserDetail> = _state.asStateFlow()

    fun isFavUser(id: Int): Boolean {
        return favoriteManager.isFavoriteItem(id)
    }

    fun setLoading(isLoading: Boolean) {
        _state.value = UserDetail.Loading(isLoading)
    }

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

    fun getUserDetail(username: String) {
        viewModelScope.launch {
            getUserDetailUserCase.getUserByUserName(username).onEach {
                when (it) {
                    is Resource.Error -> {
                        setLoading(false)
                        _state.value = UserDetail.Error(it.message)
                    }
                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        setLoading(false)
                        if (it.data == null) {
                            _state.value = UserDetail.Error(
                                UiText.StringResource(R.string.userDetailPage_emptyError)
                            )
                        } else {
                            _state.value = UserDetail.Success(data = it.data)
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UserDetail {
        object Init : UserDetail()
        data class Loading(val isLoading: Boolean) : UserDetail()
        data class Success(val data: User) : UserDetail()
        data class Error(val error: UiText) : UserDetail()
    }
}
