package com.defacto.movieapp.ui.screen.settings

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.defacto.movieapp.R
import com.defacto.movieapp.navigation.Screen
import com.defacto.movieapp.ui.component.OptionItem
import com.defacto.movieapp.ui.component.TopBar
import com.defacto.movieapp.ui.theme.DeFactoMovieAppTheme
import com.defacto.movieapp.util.LocaleHelper

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    SettingsContent(
        isDarkMode = isDarkMode,
        isUserLoggedIn = isUserLoggedIn,
        onThemeChange = { viewModel.onThemeChange(it) },
        onLogout = {
            viewModel.onLogout()
        },
        onChangePassword = {
            navController.navigate(Screen.ChangePassword.route)
        },
        onLogin = {
            navController.navigate(Screen.Login.route)
        }
    )
}

@Composable
fun SettingsContent(
    isDarkMode: Boolean,
    isUserLoggedIn: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onChangePassword: () -> Unit,
    onLogin: () -> Unit
) {
    val context = LocalContext.current
    val currentLanguage = context.resources.configuration.locales.get(0).language

    val versionName = try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: Exception) {
        "1.0"
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(stringResource(id = R.string.settings_title))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = stringResource(id = R.string.language))
                Text(
                    text = stringResource(id = R.string.turkish),
                    modifier = Modifier.clickable {                       
                        LocaleHelper.setLocale(context, "tr")
                        context.findActivity()?.recreate()
                    },
                    color = if (currentLanguage == "tr") MaterialTheme.colorScheme.primary else Color.Unspecified,
                    fontWeight = if (currentLanguage == "tr") FontWeight.Bold else FontWeight.Normal
                )
                Text(text = " / ")
                Text(
                    text = stringResource(id = R.string.english),
                    modifier = Modifier.clickable {
                        LocaleHelper.setLocale(context, "en")
                        context.findActivity()?.recreate()
                    },
                    color = if (currentLanguage == "en") MaterialTheme.colorScheme.primary else Color.Unspecified,
                    fontWeight = if (currentLanguage == "en") FontWeight.Bold else FontWeight.Normal
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                    contentDescription = stringResource(id = R.string.theme)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = isDarkMode, onCheckedChange = onThemeChange)
            }
            if (isUserLoggedIn) {
                OptionItem(title = stringResource(id = R.string.change_password), onClick = onChangePassword)
                OptionItem(title = stringResource(id = R.string.logout), onClick = onLogout)
            } else {
                OptionItem(title = stringResource(id = R.string.login), onClick = onLogin)
            }


            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.version, versionName ?: ""),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


@Preview(showBackground = true)
@Composable
fun SettingsContentPreview() {
    DeFactoMovieAppTheme {
        SettingsContent(
            isDarkMode = false,
            isUserLoggedIn = true,
            onThemeChange = {},
            onLogout = {},
            onChangePassword = {},
            onLogin = {}
        )
    }
}
