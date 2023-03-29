package com.egeysn.githubapp.domain.mappers

import com.egeysn.githubapp.common.extension.safeGet
import com.egeysn.githubapp.data.local.entities.MovieEntity
import com.egeysn.githubapp.data.remote.models.UserDto
import com.egeysn.githubapp.domain.models.User

class UserMapper {

    fun fromDtoToEntity(userDto: UserDto): MovieEntity = with(userDto) {
        MovieEntity(
            id = 123,
            posterPath = "",
            releaseDate = "",
            voteAverage = 5.2,
            title = "",
            overview = "",
            runtime = 2,
            originalTitle = "",
            popularity = 2.36,
            revenue = 3,
            status = "",
            genres = null
        )
    }

    fun fromDtoToDomain(userDto: UserDto): User = with(userDto) {
        User(
            id = id,
            avatar = avatarUrl.safeGet(),
            name = login.safeGet()
        )
    }

    fun fromEntityToDomain(userEntity: MovieEntity): User = with(userEntity) {
        User(
            id = id.toInt(),
            avatar = "",
            name = ""
        )
    }
}
