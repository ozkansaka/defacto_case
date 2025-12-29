package com.defacto.movieapp.ui.screen.movie_search.sort

import androidx.annotation.StringRes
import com.defacto.movieapp.R

enum class SortOption(@StringRes val displayName: Int) {
    ALPHABETICAL_AZ(R.string.sort_alphabetical_az),
    ALPHABETICAL_ZA(R.string.sort_alphabetical_za),
    DATE_DESC(R.string.sort_date_desc),
    RATING_DESC(R.string.sort_rating_desc)
}
