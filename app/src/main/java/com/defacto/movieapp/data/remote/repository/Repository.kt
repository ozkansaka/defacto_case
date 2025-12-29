package com.defacto.movieapp.data.remote.repository

import com.defacto.movieapp.model.Genre
import com.defacto.movieapp.model.Movie

interface Repository {
    suspend fun getSearch(query: String, page: Int): Movie
    suspend fun getGenres(): Genre
}