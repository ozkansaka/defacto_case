package com.defacto.movieapp.ui.screen.movie_search

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.defacto.movieapp.R
import com.defacto.movieapp.common.AlertDialogState
import com.defacto.movieapp.data.local.entity.SearchHistoryEntity
import com.defacto.movieapp.model.Movie
import com.defacto.movieapp.navigation.Screen
import com.defacto.movieapp.data.remote.util.UiEvent
import com.defacto.movieapp.ui.component.AlertDialog
import com.defacto.movieapp.ui.component.Button1
import com.defacto.movieapp.ui.component.CategoryDialog
import com.defacto.movieapp.ui.component.LoadingBar
import com.defacto.movieapp.ui.component.MovieCard
import com.defacto.movieapp.ui.component.SearchBar
import com.defacto.movieapp.ui.component.TopBar
import com.defacto.movieapp.ui.screen.detail.DetailArgs
import com.defacto.movieapp.ui.screen.movie_search.filter.FilterBottomSheet
import com.defacto.movieapp.ui.screen.movie_search.sort.SortBottomSheet
import com.defacto.movieapp.ui.theme.DeFactoMovieAppTheme
import com.defacto.movieapp.util.DateUtil
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun MovieSearchScreen(navController: NavHostController, viewModel: MovieSearchViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var alertDialogState by remember { mutableStateOf(AlertDialogState()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isSearchHistoryVisible by viewModel.isSearchHistoryVisible.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val showFilterBottomSheet by viewModel.showFilterBottomSheet.collectAsState()
    val showSortBottomSheet by viewModel.showSortBottomSheet.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val genres by viewModel.genres.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var selectedMovie by remember { mutableStateOf<Movie.Result?>(null) }
    val categories by viewModel.categories.collectAsState()
    var showRemoveFavoriteDialog by remember { mutableStateOf(AlertDialogState()) }
    var movieToRemoveFromFavorite by remember { mutableStateOf<Movie.Result?>(null) }
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()


    if (showFilterBottomSheet) {
        FilterBottomSheet(
            onDismiss = { viewModel.onFilterDismiss() },
            filterState = filterState,
            onFilterStateChange = { viewModel.onFilterStateChanged(it) },
            genres = genres
        )
    }

    if (showSortBottomSheet) {
        SortBottomSheet(
            onDismiss = { viewModel.onSortDismiss() },
            filterState = filterState,
            onFilterStateChange = { viewModel.onFilterStateChanged(it) },
        )
    }

    if (showCategoryDialog) {
        CategoryDialog(
            categories = categories,
            onDismiss = { showCategoryDialog = false },
            onCategorySelected = { category ->
                selectedMovie?.let {
                    viewModel.onFavoriteClick(it, category)
                }
                showCategoryDialog = false
            }
        )
    }

    AlertDialog(
        isOpen = showRemoveFavoriteDialog.isOpen,
        title = showRemoveFavoriteDialog.title,
        description = showRemoveFavoriteDialog.description,
        buttonTitle = showRemoveFavoriteDialog.buttonTitle,
        button2Title = showRemoveFavoriteDialog.button2Title,
        buttonOnClick = {
            movieToRemoveFromFavorite?.let { movie ->
                viewModel.onFavoriteClick(movie, "")
            }
            showRemoveFavoriteDialog = showRemoveFavoriteDialog.copy(isOpen = false)
        },
        button2OnClick = {
            showRemoveFavoriteDialog = showRemoveFavoriteDialog.copy(isOpen = false)
        },
        onDismiss = {
            showRemoveFavoriteDialog = showRemoveFavoriteDialog.copy(isOpen = false)
        }
    )


    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowDialog -> {
                    alertDialogState = alertDialogState.copy(
                        isOpen = true,
                        title = context.getString(R.string.warning),
                        description = event.message ?: context.getString(R.string.an_error_occurred)
                    )
                }

                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }

                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MovieSearchContent(
            movie = uiState.data,
            onClickSearch = {
                searchQuery = it
                viewModel.getSearch(it)
            },
            onClickDetail = { movie ->
                navController.navigate(Screen.Detail.route(Uri.encode(Gson().toJson(DetailArgs(movie.title, movie.overview, "https://image.tmdb.org/t/p/w500${movie.posterPath}")))))
            },
            onClickFavorite = { movie ->
                if (movie.isFavorite) {
                    movieToRemoveFromFavorite = movie
                    showRemoveFavoriteDialog = showRemoveFavoriteDialog.copy(isOpen = true, buttonTitle = context.getString(R.string.apply), button2Title = context.getString(R.string.cancel), title = context.getString(R.string.warning), description = context.getString(R.string.are_you_sure_you_want_to_remove_this_movie_from_favorites))
                } else {
                    if (isLoggedIn) {
                        selectedMovie = movie
                        showCategoryDialog = true
                    } else {
                        alertDialogState = alertDialogState.copy(
                            isOpen = true,
                            title = context.getString(R.string.warning),
                            description = context.getString(R.string.you_must_log_in_to_add_to_favorites)
                        )
                    }
                }
            },
            onSearchFocusChanged = {
                viewModel.onSearchFocusChanged(it)
            },
            isSearchHistoryVisible = isSearchHistoryVisible,
            searchHistory = searchHistory,
            onSearchHistoryClick = {
                searchQuery = it
                viewModel.getSearch(it)
            },
            onClickFilter = {
                viewModel.onFilterClick()
            },
            onClickSort = {
                viewModel.onSortClick()
            },
            onLoadMore = {
                viewModel.getSearch(searchQuery = searchQuery, isLoadMore = true)
            }
        )

        AlertDialog(
            isOpen = alertDialogState.isOpen,
            title = alertDialogState.title,
            description = alertDialogState.description,
            buttonOnClick = { alertDialogState = alertDialogState.copy(isOpen = false) },
            onDismiss = {
                alertDialogState = alertDialogState.copy(isOpen = false)
            }
        )

        LoadingBar(isVisibility = uiState.isLoading)

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun MovieSearchContent(
    movie: Movie?,
    onClickSearch: (String) -> Unit,
    onClickDetail: (Movie.Result) -> Unit,
    onClickFavorite: (Movie.Result) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    isSearchHistoryVisible: Boolean,
    searchHistory: List<SearchHistoryEntity>,
    onSearchHistoryClick: (String) -> Unit,
    onClickFilter: () -> Unit,
    onClickSort: () -> Unit,
    onLoadMore: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val lazyGridState = rememberLazyGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            lastVisibleItem.index >= lazyGridState.layoutInfo.totalItemsCount - 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            TopBar(stringResource(R.string.search_movie_title))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    SearchBar(
                        value = searchQuery,
                        hint = stringResource(id = R.string.enter_movie_name),
                        onValueChange = { value ->
                            searchQuery = value
                        },
                        modifier = Modifier.onFocusChanged { onSearchFocusChanged(it.isFocused) }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button1(
                    text = stringResource(id = R.string.search),
                ) {
                    onClickSearch(searchQuery)
                    focusManager.clearFocus()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onClickFilter) {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = stringResource(id = R.string.filter))
                }
                IconButton(onClick = onClickSort) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = stringResource(id = R.string.sort))
                }
            }

            if (isSearchHistoryVisible) {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(searchHistory) { history ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    searchQuery = history.query
                                    onSearchHistoryClick(history.query)
                                    focusManager.clearFocus()
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = history.query)
                        }
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = lazyGridState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(movie?.results ?: emptyList()) { movieResult ->
                    MovieCard(
                        image = "https://image.tmdb.org/t/p/w500${movieResult.posterPath}",
                        title = movieResult.title,
                        isFavorite = movieResult.isFavorite,
                        publicationDate = DateUtil.getYearFromDate(movieResult.releaseDate),
                        onClickDetail = {
                            onClickDetail(movieResult)
                        },
                        onClickFavorite = { onClickFavorite(movieResult) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieSearchScreenPreview() {
    DeFactoMovieAppTheme {
        MovieSearchContent(
            movie = null,
            onClickSearch = {},
            onClickDetail = { },
            onClickFavorite = {},
            onSearchFocusChanged = {},
            isSearchHistoryVisible = false,
            searchHistory = emptyList(),
            onSearchHistoryClick = {},
            onClickFilter = {},
            onClickSort = {},
            onLoadMore = {}
        )
    }
}
