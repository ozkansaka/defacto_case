package com.defacto.movieapp.ui.screen.change_password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.defacto.movieapp.R
import com.defacto.movieapp.common.AlertDialogState
import com.defacto.movieapp.common.FormFieldState
import com.defacto.movieapp.navigation.Screen
import com.defacto.movieapp.data.remote.util.UiEvent
import com.defacto.movieapp.ui.component.LoadingBar
import com.defacto.movieapp.ui.component.PasswordTextField
import com.defacto.movieapp.ui.component.Button1
import com.defacto.movieapp.ui.component.AlertDialog
import com.defacto.movieapp.ui.component.TopBarWithBackButton
import com.defacto.movieapp.ui.theme.DeFactoMovieAppTheme
import com.defacto.movieapp.util.Navigator
import com.defacto.movieapp.util.ValidationUtil

@Composable
fun ChangePasswordScreen(
    navController: NavHostController,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var alertDialogState by remember { mutableStateOf(AlertDialogState()) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowDialog -> {
                    when (event.code) {
                        "success" -> {
                            alertDialogState = alertDialogState.copy(
                                referenceType = "successChangePassword",
                                isOpen = true,
                                title = context.getString(R.string.success),
                                description = context.getString(R.string.password_changed_successfully)
                            )
                        }

                        else -> {
                            alertDialogState = alertDialogState.copy(
                                isOpen = true,
                                title = context.getString(R.string.warning),
                                description = event.message ?: context.getString(R.string.an_error_occurred)
                            )
                        }
                    }

                }

                else -> {}
            }
        }
    }

    ChangePasswordContent(onBackButton = {
        navController.navigateUp()
    }, onChangePassword = { oldPassword, password ->
        viewModel.changePassword(oldPassword, password)
    })

    AlertDialog(
        isOpen = alertDialogState.isOpen,
        title = alertDialogState.title,
        description = alertDialogState.description,
        buttonOnClick = {
            if (alertDialogState.referenceType == "successChangePassword") {
                Navigator.navigateTo(navController = navController, route = Screen.Settings.route, popUpTo = Screen.Settings.route, inclusive = true)
            }
            alertDialogState = alertDialogState.copy(isOpen = false)
        },
        onDismiss = {
            if (alertDialogState.referenceType == "successChangePassword") {
                Navigator.navigateTo(navController = navController, route = Screen.Settings.route, popUpTo = Screen.Settings.route, inclusive = true)
            }
            alertDialogState = alertDialogState.copy(isOpen = false)
        }
    )

    LoadingBar(isVisibility = uiState.isLoading)
}

@Composable
fun ChangePasswordContent(onBackButton: () -> Unit, onChangePassword: (oldPassword: String, password: String) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            TopBarWithBackButton(title = stringResource(id = R.string.change_password), onClickBackButton = onBackButton)
            ChangePasswordForm(onChangePassword = onChangePassword)

        }
    }
}

@Composable
fun ChangePasswordForm(onChangePassword: (oldPassword: String, password: String) -> Unit) {
    val context = LocalContext.current
    var oldPasswordState by remember { mutableStateOf(FormFieldState()) }
    var passwordState by remember { mutableStateOf(FormFieldState()) }
    var passwordAgainState by remember { mutableStateOf(FormFieldState()) }

    fun isValid(): Boolean {
        if (!ValidationUtil.isPasswordValid(oldPasswordState.value)) {
            oldPasswordState = oldPasswordState.copy(error = true, errorMessage = context.getString(R.string.please_enter_old_password))
        } else {
            oldPasswordState = oldPasswordState.copy(error = false, errorMessage = "")
        }

        if (!ValidationUtil.isPasswordValid(passwordState.value)) {
            passwordState = passwordState.copy(error = true, errorMessage = context.getString(R.string.enter_valid_password))
        } else {
            passwordState = passwordState.copy(error = false, errorMessage = "")
        }

        if (passwordState.value != passwordAgainState.value) {
            passwordAgainState = passwordAgainState.copy(error = true, errorMessage = context.getString(R.string.passwords_do_not_match))
        } else {
            passwordAgainState = passwordAgainState.copy(error = false, errorMessage = "")
        }

        return oldPasswordState.error == false && passwordState.error == false && passwordAgainState.error == false
    }
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Column {
            PasswordTextField(hint = stringResource(id = R.string.old_password), value = oldPasswordState.value, isError = oldPasswordState.error == true, errorMessage = oldPasswordState.errorMessage, onValueChange = {
                oldPasswordState = oldPasswordState.copy(value = it)
                oldPasswordState.error?.let {
                    isValid()
                }
            })
            Spacer(modifier = Modifier.height(24.dp))
        }
        Column {
            PasswordTextField(hint = stringResource(id = R.string.password), value = passwordState.value, isError = passwordState.error == true, errorMessage = passwordState.errorMessage, onValueChange = {
                passwordState = passwordState.copy(value = it)
                passwordState.error?.let {
                    isValid()
                }
            })
            Spacer(modifier = Modifier.height(24.dp))
        }
        Column {
            PasswordTextField(hint = stringResource(id = R.string.password_again), value = passwordAgainState.value, isError = passwordAgainState.error == true, errorMessage = passwordAgainState.errorMessage, onValueChange = {
                passwordAgainState = passwordAgainState.copy(value = it)
                passwordAgainState.error?.let {
                    isValid()
                }
            })
            Spacer(modifier = Modifier.height(24.dp))
        }
        Column {
            Button1(modifier = Modifier.fillMaxWidth(), text = stringResource(id = R.string.confirm), onClick = {
                if (isValid()) {
                    onChangePassword(oldPasswordState.value, passwordState.value)
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordFormPreview() {
    DeFactoMovieAppTheme {
        ChangePasswordContent(onBackButton = {}, onChangePassword = { _, _ -> })
    }
}
