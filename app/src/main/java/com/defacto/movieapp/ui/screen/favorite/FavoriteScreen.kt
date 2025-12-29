package com.defacto.movieapp.ui.screen.favorite

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.defacto.movieapp.R
import com.defacto.movieapp.model.Movie
import com.defacto.movieapp.navigation.Screen
import com.defacto.movieapp.ui.component.AlertDialog
import com.defacto.movieapp.ui.component.Button1
import com.defacto.movieapp.ui.component.Button2
import com.defacto.movieapp.ui.component.EditCategoryDialog
import com.defacto.movieapp.ui.component.MovieCard
import com.defacto.movieapp.ui.component.TopBar
import com.defacto.movieapp.ui.screen.detail.DetailArgs
import com.defacto.movieapp.ui.theme.DeFactoMovieAppTheme
import com.defacto.movieapp.util.DateUtil
import com.google.gson.Gson

@Composable
fun FavoriteScreen(navController: NavHostController, viewModel: FavoriteViewModel = hiltViewModel()) {
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    var categoryToDelete by remember { mutableStateOf<String?>(null) }
    var categoryToRename by remember { mutableStateOf<String?>(null) }

    if (categoryToDelete != null) {
        AlertDialog(
            isOpen = true,
            title = stringResource(id = R.string.delete_category),
            description = stringResource(id = R.string.are_you_sure_you_want_to_delete_this_category),
            buttonTitle = stringResource(id = R.string.delete),
            button2Title = stringResource(id = R.string.cancel),
            buttonOnClick = {
                viewModel.deleteCategory(categoryToDelete!!)
                categoryToDelete = null
            },
            button2OnClick = { categoryToDelete = null },
            onDismiss = { categoryToDelete = null })
    }

    if (categoryToRename != null) {
        EditCategoryDialog(
            categoryName = categoryToRename!!,
            onDismiss = { categoryToRename = null },
            onConfirm = {
                viewModel.updateCategoryName(categoryToRename!!, it)
                categoryToRename = null
            }
        )
    }

    FavoriteContent(
        movies = favoriteMovies,
        onClickFavorite = {
            viewModel.onFavoriteClick(it)
        },
        onClickDetail = { movie ->
            navController.navigate(Screen.Detail.route(Uri.encode(Gson().toJson(DetailArgs(movie.title, movie.overview, "https://image.tmdb.org/t/p/w500${movie.posterPath}")))))
        },
        onDeleteCategory = { categoryToDelete = it },
        onRenameCategory = { categoryToRename = it }
    )
}

@Composable
fun FavoriteContent(
    movies: Map<String, List<Movie.Result>>,
    onClickFavorite: (Movie.Result) -> Unit,
    onClickDetail: (Movie.Result) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onRenameCategory: (String) -> Unit,
) {
    var expandedCategories by remember { mutableStateOf(emptySet<String>()) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            TopBar(stringResource(R.string.favorites_title))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(movies.keys.toList()) { category ->
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        expandedCategories = if (expandedCategories.contains(category)) {
                                            expandedCategories - category
                                        } else {
                                            expandedCategories + category
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (expandedCategories.contains(category)) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                            IconButton(onClick = { onRenameCategory(category) }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(id = R.string.edit_category))
                            }
                            IconButton(onClick = { onDeleteCategory(category) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete_category))
                            }
                        }
                        AnimatedVisibility(visible = expandedCategories.contains(category)) {
                            Column(
                                modifier = Modifier.padding(top = 12.dp, start = 4.dp, end = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                movies[category].orEmpty().chunked(2).forEach { movieRow ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        movieRow.forEach { movie ->
                                            Box(modifier = Modifier.weight(1f)) {
                                                MovieCard(
                                                    image = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                                    title = movie.title,
                                                    isFavorite = movie.isFavorite,
                                                    publicationDate = DateUtil.getYearFromDate(movie.releaseDate),
                                                    onClickDetail = {
                                                        onClickDetail(movie)
                                                    },
                                                    onClickFavorite = { onClickFavorite(movie) }
                                                )
                                            }
                                        }
                                        if (movieRow.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditCategoryDialog(
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var newCategoryName by remember { mutableStateOf(categoryName) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.edit_category)) },
        text = {
            OutlinedTextField(
                value = newCategoryName,
                onValueChange = { newCategoryName = it },
                label = { Text(text = stringResource(id = R.string.category_name)) }
            )
        },
        confirmButton = {
            Button1(text = stringResource(id = R.string.save), onClick = { onConfirm(newCategoryName) })
        },
        dismissButton = {
            Button2(text = stringResource(id = R.string.cancel), onClick = onDismiss)
        }

    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoriteContentPreview() {
    DeFactoMovieAppTheme {
        FavoriteContent(
            movies = mapOf("Action" to emptyList(), "Comedy" to emptyList()),
            onClickFavorite = {},
            onClickDetail = {},
            onDeleteCategory = {},
            onRenameCategory = {}
        )
    }
}
