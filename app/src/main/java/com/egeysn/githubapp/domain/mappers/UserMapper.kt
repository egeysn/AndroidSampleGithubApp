package com.egeysn.githubapp.domain.mappers

import com.egeysn.githubapp.common.extension.safeGet
import com.egeysn.githubapp.data.local.entities.UserEntity
import com.egeysn.githubapp.data.remote.models.UserDto
import com.egeysn.githubapp.domain.models.User

class UserMapper {

    fun fromDtoToEntity(userDto: UserDto): UserEntity = with(userDto) {
        UserEntity(
            id = id,
            avatar = avatarUrl.safeGet(),
            name = login.safeGet()
        )
    }

    fun fromDtoToDomain(userDto: UserDto): User = with(userDto) {
        User(
            id = id,
            avatar = avatarUrl.safeGet(),
            name = login.safeGet()
        )
    }

    fun fromEntityToDomain(userEntity: UserEntity): User = with(userEntity) {
        User(
            id = userEntity.id,
            avatar = userEntity.avatar,
            name = userEntity.name
        )
    }
}
