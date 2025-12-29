package com.defacto.movieapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.defacto.movieapp.navigation.BottomNavigationBar
import com.defacto.movieapp.navigation.NavigationHost
import com.defacto.movieapp.navigation.Screen
import com.defacto.movieapp.ui.theme.DeFactoMovieAppTheme
import com.defacto.movieapp.ui.theme.White
import com.defacto.movieapp.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.onCreate(this)
        enableEdgeToEdge()
        setContent {
            val navController: NavHostController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route


            var showBottomBar by rememberSaveable { mutableStateOf(true) }

            showBottomBar = when (currentRoute) {
                Screen.MovieSearch.route,
                Screen.Favorite.route,
                Screen.Settings.route,
                    -> true

                else -> false
            }

            val isDarkMode by viewModel.isDarkMode.collectAsState()

            DeFactoMovieAppTheme(darkTheme = isDarkMode) {
                Box(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxSize()
                ) {
                    if (showBottomBar) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        ) {
                            BottomNavigationBar(
                                navController = navController
                            )
                        }

                    }
                }

                Scaffold(
                    modifier = Modifier.imePadding(),
                    content = {
                        NavigationHost(
                            navController = navController,
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize()
                        )
                    },
                    containerColor = White,
                    bottomBar = {
                        if (showBottomBar) BottomAppBar(
                            modifier = Modifier.height(64.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            BottomNavigationBar(navController = navController)
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeFactoMovieAppTheme {
        Greeting("Android")
    }
}