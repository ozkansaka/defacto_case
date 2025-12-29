package com.defacto.movieapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.defacto.movieapp.R

@Composable
fun CategoryManagementDialog(
    categories: List<String>,
    onDismiss: () -> Unit,
    onCategorySelected: (String) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onUpdateCategory: (oldCategory: String, newCategory: String) -> Unit,
    onAddNewCategory: (String) -> Unit
) {
    var showEditCategoryDialog by remember { mutableStateOf<String?>(null) }
    var showDeleteCategoryDialog by remember { mutableStateOf<String?>(null) }
    var showAddNewCategoryDialog by remember { mutableStateOf(false) }

    if (showEditCategoryDialog != null) {
        EditCategoryDialog(
            categoryName = showEditCategoryDialog!!,
            onDismiss = { showEditCategoryDialog = null },
            onConfirm = {
                onUpdateCategory(showEditCategoryDialog!!, it)
                showEditCategoryDialog = null
            }
        )
    }

    if (showDeleteCategoryDialog != null) {
        AlertDialog(
            isOpen = true,
            title = stringResource(id = R.string.delete_category),
            description = stringResource(id = R.string.are_you_sure_you_want_to_delete_this_category),
            buttonTitle = stringResource(id = R.string.delete),
            button2Title = stringResource(id = R.string.cancel),
            buttonOnClick = {
                onDeleteCategory(showDeleteCategoryDialog!!)
                showDeleteCategoryDialog = null
            },
            button2OnClick = { showDeleteCategoryDialog = null },
            onDismiss = { showDeleteCategoryDialog = null })
    }

    if (showAddNewCategoryDialog) {
        AddNewCategoryDialog(
            onDismiss = { showAddNewCategoryDialog = false },
            onConfirm = {
                onAddNewCategory(it)
                showAddNewCategoryDialog = false
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.manage_categories), style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(categories) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = it, modifier = Modifier.weight(1f))
                            Row {
                                IconButton(onClick = { showEditCategoryDialog = it }) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(id = R.string.edit_category))
                                }
                                IconButton(onClick = { showDeleteCategoryDialog = it }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete_category))
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showAddNewCategoryDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(id = R.string.add_new_category))
                }
            }
        }
    }
}

@Composable
fun EditCategoryDialog(
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
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
            Button(onClick = { onConfirm(newCategoryName) }) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
fun AddNewCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newCategoryName by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.add_new_category)) },
        text = {
            OutlinedTextField(
                value = newCategoryName,
                onValueChange = { newCategoryName = it },
                label = { Text(text = stringResource(id = R.string.category_name)) }
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(newCategoryName) },
                enabled = newCategoryName.isNotBlank()
            ) {
                Text(text = stringResource(id = R.string.add))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}
