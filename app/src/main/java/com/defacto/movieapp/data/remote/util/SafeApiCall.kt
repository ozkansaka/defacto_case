package com.defacto.movieapp.data.remote.util

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

suspend fun <T> apiCall(block: suspend () -> T): Result<T> {
    return try {
        val response = block()
        Result.Success(response)
    } catch (e: HttpException) {
        if (e.code() == 401) {
            return Result.Success(null as T?)
        }
        return Result.Error(code = e.code().toString(), message = null)

    } catch (e: IOException) {
        if (e is UnknownHostException) {
            return Result.Error(code = null, message = "Lütfen internet bağlantınızı kontrol edin.")
        }
        return Result.Error(code = null, message = null)

    }
}

