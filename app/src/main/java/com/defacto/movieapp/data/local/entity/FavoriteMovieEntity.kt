package com.defacto.movieapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_movies",
    indices = [Index(value = ["userId", "movieId"], unique = true)]
)
data class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val movieId: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val category: String,
    val releaseDate: String?,
    val overview: String?,
    val createdAt: Long = System.currentTimeMillis(),
)
