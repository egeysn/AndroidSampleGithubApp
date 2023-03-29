package com.egeysn.githubapp.data.remote.models

import com.egeysn.githubapp.data.local.entities.GenreEntity

data class GenreDto(
    val id: Int,
    val name: String
)

fun GenreDto.toGenreEntity(): GenreEntity = GenreEntity(
    id = id,
    name = name
)
