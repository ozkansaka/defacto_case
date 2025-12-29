package com.defacto.movieapp.ui.screen.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.defacto.movieapp.R
import com.defacto.movieapp.common.AlertDialogState
import com.defacto.movieapp.common.FormFieldState
import com.defacto.movieapp.navigation.Screen
import com.defacto.movieapp.data.remote.util.UiEvent
import com.defacto.movieapp.ui.component.CustomTextField
import com.defacto.movieapp.ui.component.LoadingBar
import com.defacto.movieapp.ui.component.PasswordTextField
import com.defacto.movieapp.ui.component.Button1
import com.defacto.movieapp.ui.component.AlertDialog
import com.defacto.movieapp.ui.component.TopBarWithBackButton
import com.defacto.movieapp.ui.theme.Blue900
import com.defacto.movieapp.ui.theme.DeFactoMovieAppTheme
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.util.Navigator
import com.defacto.movieapp.util.ValidationUtil

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var alertDialogState by remember { mutableStateOf(AlertDialogState()) }

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

                is UiEvent.Action -> {
                    Navigator.navigateTo(navController, route = Screen.Login.route)
                }

                else -> {}
            }
        }
    }

    RegisterContent(onBackButton = {
        navController.navigateUp()
    }, onRegister = { email, password ->
        viewModel.register(email, password)
    })

    AlertDialog(
        isOpen = alertDialogState.isOpen,
        title = alertDialogState.title,
        description = alertDialogState.description,
        buttonOnClick = {
            alertDialogState = alertDialogState.copy(isOpen = false)
        },
        onDismiss = {
            alertDialogState = alertDialogState.copy(isOpen = false)
        }
    )

    LoadingBar(isVisibility = uiState.isLoading)
}

@Composable
fun RegisterContent(onBackButton: () -> Unit, onRegister: (email: String, password: String) -> Unit) {
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
            TopBarWithBackButton(title = "", onClickBackButton = onBackButton)

            Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = stringResource(id = R.string.register), fontSize = 32.sp, color = Blue900, style = PoppinsFontStyle.bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            RegisterForm(onRegister = onRegister)
        }
    }
}

@Composable
fun RegisterForm(onRegister: (email: String, password: String) -> Unit) {
    val context = LocalContext.current
    var emailState by remember { mutableStateOf(FormFieldState()) }
    var passwordState by remember { mutableStateOf(FormFieldState()) }
    var passwordAgainState by remember { mutableStateOf(FormFieldState()) }

    fun isValid(): Boolean {
        if (!ValidationUtil.isEmailValid(emailState.value)) {
            emailState = emailState.copy(error = true, errorMessage = context.getString(R.string.enter_valid_email))
        } else {
            emailState = emailState.copy(error = false, errorMessage = "")
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

        return emailState.error == false && passwordState.error == false && passwordAgainState.error == false
    }
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Column {
            CustomTextField(hint = stringResource(id = R.string.email), value = emailState.value, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), isError = emailState.error == true, errorMessage = emailState.errorMessage, onValueChange = {
                emailState = emailState.copy(value = it)
                emailState.error?.let {
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
            Button1(modifier = Modifier.fillMaxWidth(), text = stringResource(id = R.string.register_button), onClick = {
                if (isValid()) {
                    onRegister(emailState.value, passwordState.value)
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterFormPreview() {
    DeFactoMovieAppTheme {
        RegisterContent(onBackButton = {}, onRegister = { _, _ -> })
    }
}
