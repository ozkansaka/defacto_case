package com.defacto.movieapp.data.repository

import com.defacto.movieapp.data.local.dao.UserDao
import com.defacto.movieapp.data.local.entity.UserEntity
import com.defacto.movieapp.data.session.SessionManager
import com.defacto.movieapp.data.remote.util.Result
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {
    suspend fun login(email: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.login(email, password)

            if (user != null) {
                sessionManager.saveUserId(user.id)
                Result.Success(user)
            } else {
                Result.Error(
                    code = "AUTH_001",
                    message = "Email veya şifre hatalı"
                )
            }
        } catch (e: Exception) {
            Result.Error(
                code = "AUTH_999",
                message = e.localizedMessage ?: "Bilinmeyen hata"
            )
        }
    }

    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            val existingUser = userDao.getUserByEmail(email)

            if (existingUser != null) {
                Result.Error(
                    code = "AUTH_002",
                    message = "Bu email ile kayıtlı kullanıcı var"
                )
            } else {
                userDao.insertUser(
                    UserEntity(
                        email = email,
                        password = password
                    )
                )
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Error(
                code = "AUTH_999",
                message = e.localizedMessage ?: "Bilinmeyen hata"
            )
        }
    }

    suspend fun logout() {
        sessionManager.clear()
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return try {
            val userId = sessionManager.userIdFlow.first() ?:return Result.Error(
                code = "AUTH_003",
                message = "Kullanıcı bulunamadı"
            )

            val user = userDao.getUserById(userId)

            if (user == null) {
                Result.Error(
                    code = "AUTH_003",
                    message = "Kullanıcı bulunamadı"
                )
            } else if (user.password != oldPassword) {
                Result.Error(
                    code = "AUTH_004",
                    message = "Eski şifre hatalı"
                )
            } else {
                userDao.updateUser(user.copy(password = newPassword))
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Error(
                code = "AUTH_999",
                message = e.localizedMessage ?: "Bilinmeyen hata"
            )
        }
    }
}
