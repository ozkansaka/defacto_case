package com.defacto.movieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.defacto.movieapp.data.local.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE userId = :userId AND movieId = :movieId")
    suspend fun delete(userId: Long, movieId: Int)

    @Query("DELETE FROM favorite_movies WHERE userId = :userId AND category = :category")
    suspend fun deleteCategory(userId: Long, category: String)

    @Query("UPDATE favorite_movies SET category = :newCategoryName WHERE userId = :userId AND category = :oldCategoryName")
    suspend fun updateCategoryName(userId: Long, oldCategoryName: String, newCategoryName: String)

    @Query(" SELECT * FROM favorite_movies WHERE userId = :userId ORDER BY createdAt DESC")
    fun getFavorites(userId: Long): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE userId = :userId AND movieId = :movieId)")
    suspend fun isFavorite(userId: Long, movieId: Int): Boolean

    @Query("SELECT DISTINCT category FROM favorite_movies WHERE userId = :userId ORDER BY category ASC")
    fun getCategories(userId: Long): Flow<List<String>>
}
