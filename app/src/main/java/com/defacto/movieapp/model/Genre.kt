package com.defacto.movieapp.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("genres")
    val genres: List<Result>,
) {
    data class Result(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
    )
}