package com.defacto.movieapp.ui.screen.movie_search.filter

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.defacto.movieapp.R
import com.defacto.movieapp.model.Genre
import com.defacto.movieapp.ui.component.Button1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    filterState: FilterState,
    onFilterStateChange: (FilterState) -> Unit,
    genres: List<Genre.Result>
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    var showGenreOptions by remember { mutableStateOf(false) }
    var tempFilterState by remember { mutableStateOf(filterState) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.filter),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = tempFilterState.releaseYear?.toString() ?: "",
                onValueChange = { tempFilterState = tempFilterState.copy(releaseYear = it.toIntOrNull()) },
                label = { Text(stringResource(id = R.string.year)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = tempFilterState.voteAverage?.toString() ?: "",
                onValueChange = { tempFilterState = tempFilterState.copy(voteAverage = it.toFloatOrNull()) },
                label = { Text(stringResource(id = R.string.rating_min)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.genre),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = showGenreOptions,
                onExpandedChange = { showGenreOptions = !showGenreOptions }
            ) {
                val selectedGenreName =
                    genres.find { it.id == tempFilterState.selectedGenreId }?.name
                        ?: stringResource(id = R.string.all)
                TextField(
                    value = selectedGenreName,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            RoundedCornerShape(4.dp)
                        )
                )

                ExposedDropdownMenu(
                    expanded = showGenreOptions,
                    onDismissRequest = { showGenreOptions = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.all)) },
                        onClick = {
                            tempFilterState = tempFilterState.copy(selectedGenreId = null)
                            showGenreOptions = false
                        }
                    )
                    genres.forEach { genre ->
                        DropdownMenuItem(
                            text = { Text(genre.name) },
                            onClick = {
                                tempFilterState = tempFilterState.copy(selectedGenreId = genre.id)
                                showGenreOptions = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button1(text = stringResource(id = R.string.apply), onClick = {
                onFilterStateChange(tempFilterState)
                onDismiss()
            })
        }
    }
}