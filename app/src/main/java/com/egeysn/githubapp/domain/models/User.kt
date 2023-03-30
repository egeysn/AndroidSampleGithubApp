package com.egeysn.githubapp.domain.models

data class User(
    val id: Int,
    val avatar: String,
    val name: String,
    val location: String? = "",
    val company: String? = "",
    val bio: String?
)
