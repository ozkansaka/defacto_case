package com.defacto.movieapp.util


import androidx.navigation.NavHostController

object Navigator {
    fun navigateTo(
        navController: NavHostController,
        route: String,
        args: Map<String, Any?>? = null,
        popUpTo: String? = null,
        inclusive: Boolean = false,
        saveState: Boolean = false,
        launchSingleTop: Boolean = false,
        restoreState: Boolean = false
    ) {
        var finalRoute = route
        args?.let {
            val argsString = it.map { (key, value) ->
                "$key=$value"
            }.joinToString("&")
            finalRoute = "$route?$argsString"
        }

        navController.navigate(finalRoute) {
            popUpTo?.let {
                this.popUpTo(popUpTo) {
                    this.inclusive = inclusive
                    this.saveState = saveState
                }
            }
            this.launchSingleTop = launchSingleTop
            this.restoreState = restoreState
        }
    }
}