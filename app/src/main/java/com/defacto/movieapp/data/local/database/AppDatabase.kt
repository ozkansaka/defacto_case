package com.defacto.movieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.defacto.movieapp.data.local.dao.FavoriteMovieDao
import com.defacto.movieapp.data.local.dao.SearchHistoryDao
import com.defacto.movieapp.data.local.dao.UserDao
import com.defacto.movieapp.data.local.entity.FavoriteMovieEntity
import com.defacto.movieapp.data.local.entity.SearchHistoryEntity
import com.defacto.movieapp.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        FavoriteMovieEntity::class,
        SearchHistoryEntity::class
    ],
    version = 7
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}