package com.defacto.movieapp.data.remote.util

sealed class Result<out T> {
    data class Success<T>(val data: T?): Result<T>()
    data class Error(val code: String?, val message: String?): Result<Nothing>()
}