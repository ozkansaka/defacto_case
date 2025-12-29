package com.defacto.movieapp.ui.screen.movie_search.filter

import com.defacto.movieapp.ui.screen.movie_search.sort.SortOption

data class FilterState(
    val releaseYear: Int? = null,
    val voteAverage: Float? = null,
    val selectedGenreId: Int? = null,
    val sortOption: SortOption = SortOption.DATE_DESC
)
