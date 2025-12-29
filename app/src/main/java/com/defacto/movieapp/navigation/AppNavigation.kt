package com.defacto.movieapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.defacto.movieapp.ui.screen.change_password.ChangePasswordScreen
import com.defacto.movieapp.ui.screen.detail.DetailArgs
import com.defacto.movieapp.ui.screen.detail.DetailScreen
import com.defacto.movieapp.ui.screen.favorite.FavoriteScreen
import com.defacto.movieapp.ui.screen.login.LoginScreen
import com.defacto.movieapp.ui.screen.movie_search.MovieSearchScreen
import com.defacto.movieapp.ui.screen.register.RegisterScreen
import com.defacto.movieapp.ui.screen.settings.SettingsScreen
import com.defacto.movieapp.ui.screen.splash.SplashScreen
import com.google.gson.Gson

sealed interface Screen {
    val route: String

    data object Splash : Screen {
        override val route = "splash"
    }

    data object Login : Screen {
        override val route = "login"
    }

    data object Register : Screen {
        override val route = "register"
    }

    data object MovieSearch : Screen {
        override val route = "movie_search"
    }

    data object Favorite : Screen {
        override val route = "favorite"
    }

    data object Detail : Screen {
        const val ROUTE_BASE = "detail"
        const val ARGS = "args"
        override val route = "$ROUTE_BASE/{$ARGS}"
        fun route(args: String): String = "$ROUTE_BASE/$args"
    }

    data object Settings : Screen {
        override val route = "setting"
    }

    data object ChangePassword : Screen {
        override val route = "change_password"
    }
}


@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.MovieSearch.route) {
            MovieSearchScreen(navController = navController)
        }
        composable(route = Screen.Favorite.route) {
            FavoriteScreen(navController = navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(route = Screen.ChangePassword.route) {
            ChangePasswordScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route, arguments = listOf(
                navArgument(Screen.Detail.ARGS) {
                    type = NavType.StringType
                    nullable = true
                }
            )) { backStackEntry ->
            val args = backStackEntry.arguments?.getString(Screen.Detail.ARGS)
            DetailScreen(navController = navController, args = Gson().fromJson(args, DetailArgs::class.java))
        }
    }
}


