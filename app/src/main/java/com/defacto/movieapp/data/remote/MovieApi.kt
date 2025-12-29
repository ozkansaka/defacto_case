package com.defacto.movieapp.data.remote

import com.defacto.movieapp.common.Constants
import com.defacto.movieapp.model.Genre
import com.defacto.movieapp.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/3/search/movie?language=tr-TR&api_key=${Constants.API_KEY}")
    suspend fun getSearch(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Movie

    @GET("/3/genre/movie/list?language=tr-TR&api_key=${Constants.API_KEY}")
    suspend fun getGenres(): Genre
}