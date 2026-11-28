package com.hiyako.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hiyako.app.ui.screens.DetailsScreen
import com.hiyako.app.ui.screens.HomeScreen
import com.hiyako.app.ui.screens.LibraryScreen
import com.hiyako.app.ui.screens.MyListScreen
import com.hiyako.app.ui.screens.ProfileScreen

@Composable
fun HiyakoNavHost(navController: NavHostController, onMenuClick: () -> Unit, modifier: Modifier = Modifier) {
    val open: (String) -> Unit = { id -> navController.navigate(Routes.details(id)) }
    val search: () -> Unit = { navController.navigateToTab(Routes.LIBRARY) }
    NavHost(navController = navController, startDestination = Routes.HOME, modifier = modifier) {
        composable(Routes.HOME) { HomeScreen(onMenu = onMenuClick, onOpen = open, onSearch = search) }
        composable(Routes.MYLIST) { MyListScreen(onOpen = open) }
        composable(Routes.LIBRARY) { LibraryScreen(onOpen = open) }
        composable(Routes.PROFILE) { ProfileScreen(onOpen = open) }
        composable(
            Routes.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            DetailsScreen(
                id = entry.arguments?.getString("id").orEmpty(),
                onBack = { navController.popBackStack() },
                onOpen = open
            )
        }
    }
}
