package com.defacto.movieapp.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.defacto.movieapp.R
import com.defacto.movieapp.ui.theme.Blue900
import com.defacto.movieapp.ui.theme.DarkNavy
import com.defacto.movieapp.ui.theme.Orange300
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.ui.theme.White

data class BarItem(
    val title: String,
    val icon: ImageVector? = null,
    val route: String,
)

object NavBarItems {
    val BarItems @Composable get() = listOf(
        BarItem(
            title = stringResource(id = R.string.home),
            icon = Icons.Default.Home,
            route = Screen.MovieSearch.route
        ),

        BarItem(
            title = stringResource(id = R.string.favorites),
            icon = Icons.Default.Favorite,
            route = Screen.Favorite.route
        ),

        BarItem(
            title = stringResource(id = R.string.settings),
            icon = Icons.Default.Settings,
            route = Screen.Settings.route
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = DarkNavy,
        contentColor = White,
        windowInsets = WindowInsets(0.dp,0.dp,0.dp,12.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavBarItems.BarItems.forEach { navBarItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Blue900,
                    selectedTextColor = Orange300,
                    unselectedIconColor = White,
                    unselectedTextColor = White,
                    indicatorColor = DarkNavy,
                ),
                selected = currentDestination?.route == navBarItem.route,
                onClick = {
                    if (navBarItem.route != "") {
                        navController.navigate(navBarItem.route) {
                            if (currentDestination != null) {
                                popUpTo(currentDestination.id) {
                                    saveState = true
                                    inclusive = true
                                }
                            } else {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                    inclusive = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    navBarItem.icon?.let {
                        Icon(
                            imageVector = navBarItem.icon,
                            contentDescription = ""
                        )
                    }
                },
                label = {
                    Text(text = navBarItem.title, fontSize = 10.sp, color = White, style = PoppinsFontStyle.normal)
                },
            )
        }
    }
}
