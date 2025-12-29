package com.defacto.movieapp.ui.screen.movie_search.sort

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.defacto.movieapp.R
import com.defacto.movieapp.ui.component.Button1
import com.defacto.movieapp.ui.screen.movie_search.filter.FilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    onDismiss: () -> Unit,
    filterState: FilterState,
    onFilterStateChange: (FilterState) -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    var showSortOptions by remember { mutableStateOf(false) }
    var tempFilterState by remember { mutableStateOf(filterState) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.sort),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = showSortOptions,
                onExpandedChange = { showSortOptions = !showSortOptions }
            ) {
                TextField(
                    value = stringResource(id = tempFilterState.sortOption.displayName),
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
                    expanded = showSortOptions,
                    onDismissRequest = { showSortOptions = false }
                ) {
                    SortOption.values().forEach { sortOption ->
                        DropdownMenuItem(
                            text = { Text(stringResource(id = sortOption.displayName)) },
                            onClick = {
                                tempFilterState = tempFilterState.copy(sortOption = sortOption)
                                showSortOptions = false
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
