package com.egeysn.githubapp.presentation.favoriteManager

import com.egeysn.githubapp.data.local.entities.FavEntity
import com.egeysn.githubapp.data.repositories.GithubRepositoryImpl
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoriteManager @Inject constructor(private val repository: GithubRepositoryImpl) {

    private val _favoriteList = MutableStateFlow<ArrayList<Int>>(arrayListOf())
    val favoriteList: StateFlow<ArrayList<Int>> = _favoriteList

    init {
        getFavorites()
    }
    fun saveFavorite(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                repository.insertFavorite(FavEntity(id))
                _favoriteList.value.add(id)
            } catch (_: Exception) {
            }
        }
    }

    fun deleteFavorite(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                repository.insertFavorite(FavEntity(id))
                _favoriteList.value.remove(id)
            } catch (_: Exception) {
            }
        }
    }

    private fun getFavorites() {
        GlobalScope.launch(Dispatchers.IO) {
            val favList = repository.getFavorites().map { it.id }
            _favoriteList.value = favList as ArrayList<Int>
        }
    }

    fun isFavoriteItem(id: Int): Boolean {
        return _favoriteList.value.contains(id)
    }

/*    companion object {
        @Volatile
        private var instance: FavoriteManager? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FavoriteManager().also { instance = it }
        }
    }*/
}
