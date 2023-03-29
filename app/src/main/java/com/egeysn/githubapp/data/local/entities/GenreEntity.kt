package com.egeysn.githubapp.data.local.entities

import androidx.room.Entity

@Entity
data class GenreEntity(
    val id: Int,
    val name: String
)
