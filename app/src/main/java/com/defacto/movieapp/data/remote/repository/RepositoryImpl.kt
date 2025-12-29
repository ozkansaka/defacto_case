package com.defacto.movieapp.data.remote.repository

import com.defacto.movieapp.model.Genre
import com.defacto.movieapp.model.Movie
import com.defacto.movieapp.data.remote.MovieApi
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: MovieApi
) : Repository {
    override suspend fun getSearch(query: String, page: Int): Movie {
        return api.getSearch(query = query, page = page)
    }

    override suspend fun getGenres(): Genre {
        return api.getGenres()
    }
}