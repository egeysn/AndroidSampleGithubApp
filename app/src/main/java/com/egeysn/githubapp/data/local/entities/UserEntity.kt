package com.egeysn.githubapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val avatar: String,
    val name: String,
    val location: String?,
    val company: String?,
    val bio: String?,
    val followers: Int?,
    val isFav: Boolean = false
)
