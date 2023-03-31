package com.egeysn.githubapp.domain.mappers

import com.egeysn.githubapp.common.extension.safeGet
import com.egeysn.githubapp.data.local.entities.UserEntity
import com.egeysn.githubapp.data.remote.models.UserDto
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.presentation.favoriteManager.FavoriteManager
import javax.inject.Inject

class UserMapper @Inject constructor() {
    @Inject
    lateinit var favoriteManager: FavoriteManager

    fun fromDtoToEntity(userDto: UserDto): UserEntity = with(userDto) {
        UserEntity(
            id = id,
            avatar = avatarUrl.safeGet(),
            name = login.safeGet(),
            company = company,
            location = location,
            bio = bio,
            followers = followers,
            // isFav = favoriteManager.favoriteList.value.contains(id)
        )
    }

    fun fromDtoToDomain(userDto: UserDto): User = with(userDto) {
        User(
            id = id,
            avatar = avatarUrl.safeGet(),
            name = login.safeGet(),
            location = location,
            company = company ?: "-",
            bio = bio,
            followers = followers,
            // isFav = favoriteManager.favoriteList.value.contains(id)
        )
    }

    fun fromEntityToDomain(userEntity: UserEntity): User = with(userEntity) {
        User(
            id = id,
            avatar = avatar,
            name = name,
            location = location,
            company = company,
            bio = bio,
            followers = followers,
            // isFav = favoriteManager.favoriteList.value.contains(id)
        )
    }
}
