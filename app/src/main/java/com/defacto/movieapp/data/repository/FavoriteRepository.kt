package com.defacto.movieapp.data.repository

import com.defacto.movieapp.data.local.dao.FavoriteMovieDao
import com.defacto.movieapp.data.local.entity.FavoriteMovieEntity
import com.defacto.movieapp.data.session.SessionManager
import com.defacto.movieapp.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteMovieDao,
    private val sessionManager: SessionManager,
) {

    suspend fun toggleFavorite(movie: Movie.Result, category: String?) {
        val userId = sessionManager.userIdFlow.first() ?: return
        val movieId = movie.id ?: 0

        val isFav = favoriteDao.isFavorite(userId, movieId)

        if (isFav) {
            favoriteDao.delete(userId, movieId)
        } else {
            if (category != null && category.isNotBlank()) {
                favoriteDao.insert(
                    FavoriteMovieEntity(
                        userId = userId,
                        movieId = movieId,
                        title = movie.title ?: "",
                        posterPath = movie.posterPath,
                        voteAverage = movie.voteAverage ?: 0.0,
                        category = category,
                        releaseDate = movie.releaseDate,
                        overview = movie.overview
                    )
                )
            }
        }
    }

    suspend fun deleteCategory(category: String) {
        val userId = sessionManager.userIdFlow.first() ?: return
        favoriteDao.deleteCategory(userId, category)
    }

    suspend fun updateCategoryName(oldCategoryName: String, newCategoryName: String) {
        val userId = sessionManager.userIdFlow.first() ?: return
        favoriteDao.updateCategoryName(userId, oldCategoryName, newCategoryName)
    }

    fun getFavorites(): Flow<List<FavoriteMovieEntity>> {
        return sessionManager.userIdFlow.flatMapLatest { userId ->
            userId?.let {
                favoriteDao.getFavorites(it)
            } ?: flowOf(emptyList())
        }
    }

    fun getCategories(): Flow<List<String>> {
        return sessionManager.userIdFlow.flatMapLatest { userId ->
            userId?.let {
                favoriteDao.getCategories(it)
            } ?: flowOf(emptyList())
        }
    }
}
