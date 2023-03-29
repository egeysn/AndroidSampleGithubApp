package com.egeysn.githubapp.data.remote.models

import com.egeysn.githubapp.domain.models.ErrorModel

data class ErrorDto(val error: String?)

fun ErrorDto.toErrorModel(): ErrorModel = ErrorModel(error = error)
