package com.defacto.movieapp.util

object ValidationUtil {
    fun isEmailValid(email: String?): Boolean {
        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        return email?.matches(emailRegex.toRegex()) ?: false
    }

    fun isPasswordValid(password: String?): Boolean {
        password?.let {
            return password.trim { it <= ' ' }.length >= 6
        }
        return false
    }
}