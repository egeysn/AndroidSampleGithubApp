package com.egeysn.githubapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavEntity(
    @PrimaryKey
    val id: Int
)
