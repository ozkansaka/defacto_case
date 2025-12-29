package com.defacto.movieapp.di

import android.content.Context
import androidx.room.Room
import com.defacto.movieapp.data.local.dao.FavoriteMovieDao
import com.defacto.movieapp.data.local.dao.SearchHistoryDao
import com.defacto.movieapp.data.local.dao.UserDao
import com.defacto.movieapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao =
        db.userDao()

    @Provides
    fun provideFavoriteMovieDao(db: AppDatabase): FavoriteMovieDao =
        db.favoriteMovieDao()

    @Provides
    fun provideSearchHistoryDao(db: AppDatabase): SearchHistoryDao =
        db.searchHistoryDao()
}