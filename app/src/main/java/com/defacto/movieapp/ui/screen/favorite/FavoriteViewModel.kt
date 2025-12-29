package com.defacto.movieapp.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defacto.movieapp.data.repository.FavoriteRepository
import com.defacto.movieapp.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favoriteMovies = MutableStateFlow<Map<String, List<Movie.Result>>>(emptyMap())
    val favoriteMovies: StateFlow<Map<String, List<Movie.Result>>> = _favoriteMovies

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            favoriteRepository.getFavorites()
                .map { favoriteEntities ->
                    favoriteEntities.groupBy { it.category }
                        .mapValues { entry ->
                            entry.value.map { entity ->
                                Movie.Result(
                                    id = entity.movieId,
                                    title = entity.title,
                                    posterPath = entity.posterPath,
                                    voteAverage = entity.voteAverage,
                                    releaseDate = entity.releaseDate,
                                    overview = entity.overview,
                                    isFavorite = true
                                )
                            }
                        }
                }
                .collect { movies ->
                    _favoriteMovies.value = movies
                }
        }
    }

    fun onFavoriteClick(movie: Movie.Result) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(movie, null)
        }
    }

    fun deleteCategory(category: String) {
        viewModelScope.launch {
            favoriteRepository.deleteCategory(category)
        }
    }

    fun updateCategoryName(oldCategoryName: String, newCategoryName: String) {
        viewModelScope.launch {
            favoriteRepository.updateCategoryName(oldCategoryName, newCategoryName)
        }
    }
}
