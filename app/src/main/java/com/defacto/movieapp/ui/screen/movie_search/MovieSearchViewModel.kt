package com.defacto.movieapp.ui.screen.movie_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defacto.movieapp.data.local.entity.SearchHistoryEntity
import com.defacto.movieapp.data.repository.FavoriteRepository
import com.defacto.movieapp.data.repository.SearchHistoryRepository
import com.defacto.movieapp.data.session.SessionManager
import com.defacto.movieapp.model.Genre
import com.defacto.movieapp.model.Movie
import com.defacto.movieapp.data.remote.util.Result
import com.defacto.movieapp.data.remote.util.UiEvent
import com.defacto.movieapp.data.remote.util.UiState
import com.defacto.movieapp.data.remote.util.apiCall
import com.defacto.movieapp.data.remote.repository.Repository
import com.defacto.movieapp.ui.screen.movie_search.filter.FilterState
import com.defacto.movieapp.ui.screen.movie_search.sort.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val repository: Repository,
    private val favoriteRepository: FavoriteRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<Movie>())
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _isSearchHistoryVisible = MutableStateFlow(false)
    val isSearchHistoryVisible = _isSearchHistoryVisible.asStateFlow()

    private val _showFilterBottomSheet = MutableStateFlow(false)
    val showFilterBottomSheet = _showFilterBottomSheet.asStateFlow()

    private val _showSortBottomSheet = MutableStateFlow(false)
    val showSortBottomSheet = _showSortBottomSheet.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState = _filterState.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre.Result>>(emptyList())
    val genres = _genres.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = sessionManager.userIdFlow.map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val searchHistory: StateFlow<List<SearchHistoryEntity>> = searchHistoryRepository.getSearchHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = favoriteRepository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val searchResult = MutableStateFlow<Movie?>(null)
    private val favorites = favoriteRepository.getFavorites()

    private var currentPage = 1
    private var currentSearchQuery = ""

    init {
        getGenres()
        viewModelScope.launch {
            combine(
                searchResult, favorites, _filterState
            ) { searchResult, favorites, filterState ->
                searchResult?.let {
                    val favoriteIds = favorites.map { it.movieId }.toSet()
                    var updatedResults = it.results.map { movie ->
                        movie.copy(isFavorite = favoriteIds.contains(movie.id))
                    }

                    filterState.releaseYear?.let {
                        updatedResults = updatedResults.filter { movie -> movie.releaseDate?.startsWith(it.toString()) == true }
                    }
                    filterState.voteAverage?.let {
                        updatedResults = updatedResults.filter { movie -> (movie.voteAverage ?: 0.0) >= it }
                    }
                    filterState.selectedGenreId?.let {
                        updatedResults = updatedResults.filter { movie -> movie.genreIds?.contains(it) == true }
                    }

                    updatedResults = when (filterState.sortOption) {
                        SortOption.ALPHABETICAL_AZ -> updatedResults.sortedBy { it.title }
                        SortOption.ALPHABETICAL_ZA -> updatedResults.sortedByDescending { it.title }
                        SortOption.DATE_DESC -> updatedResults.sortedByDescending { it.releaseDate }
                        SortOption.RATING_DESC -> updatedResults.sortedByDescending { it.voteAverage }
                    }

                    it.copy(results = updatedResults)
                }
            }.collect { movie ->
                _uiState.update { it.copy(data = movie) }
            }
        }
    }

    private fun getGenres() {
        viewModelScope.launch {
            when (val result = apiCall { repository.getGenres() }) {
                is Result.Success -> {
                    _genres.value = result.data?.genres ?: emptyList()
                }

                is Result.Error -> {
                }
            }
        }
    }

    fun getSearch(searchQuery: String, isLoadMore: Boolean = false) {
        viewModelScope.launch {

            if (searchQuery.isBlank() && !isLoadMore) {
                _uiEvents.emit(UiEvent.ShowSnackbar("Lütfen bir film adı girin."))
                return@launch
            }

            if (!isLoadMore) {
                currentPage = 1
                currentSearchQuery = searchQuery
                searchHistoryRepository.addSearchHistory(searchQuery)
                _uiState.update { it.copy(data = null) }
            } else {
                currentPage++
            }

            _isSearchHistoryVisible.value = false
            _uiState.update { it.copy(isLoading = true) }

            when (val result = apiCall {
                repository.getSearch(query = currentSearchQuery, page = currentPage)
            }) {
                is Result.Success -> {
                    val newMovies = result.data
                    if (isLoadMore) {
                        val currentMovies = searchResult.value?.results ?: emptyList()
                        val allMovies = currentMovies + (newMovies?.results ?: emptyList())
                        searchResult.value = newMovies?.copy(results = allMovies)
                    } else {
                        searchResult.value = newMovies
                    }
                    _uiState.update { it.copy(isLoading = false) }
                }

                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvents.emit(UiEvent.ShowDialog(result.code, result.message))
                }
            }
        }
    }

    fun onFavoriteClick(movie: Movie.Result, category: String) {
        viewModelScope.launch {
            val isUserLoggedIn = sessionManager.userIdFlow.first() != null
            if (isUserLoggedIn) {
                favoriteRepository.toggleFavorite(movie, category)
            } else {
                _uiEvents.emit(UiEvent.ShowDialog(code = "AUTH_003", message = "Favorilere eklemek için giriş yapmalısınız."))
            }
        }
    }

    fun onSearchFocusChanged(isFocused: Boolean) {
        _isSearchHistoryVisible.value = isFocused && !showFilterBottomSheet.value
    }

    fun onFilterClick() {
        _showFilterBottomSheet.value = true
    }

    fun onFilterDismiss() {
        _showFilterBottomSheet.value = false
    }

    fun onSortClick() {
        _showSortBottomSheet.value = true
    }

    fun onSortDismiss() {
        _showSortBottomSheet.value = false
    }

    fun onFilterStateChanged(newFilterState: FilterState) {
        _filterState.value = newFilterState
    }
}
