package com.defacto.movieapp.data.remote.util

data class UiState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
)