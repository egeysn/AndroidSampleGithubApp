package com.egeysn.githubapp.data.remote.models.response

import com.egeysn.githubapp.data.remote.models.UserDto
import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean?,
    @SerializedName("items")
    val items: List<UserDto>,
    @SerializedName("total_count")
    val totalCount: Int?
)
