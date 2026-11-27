package com.hiyako.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hiyako.app.ui.screens.HomeScreen
import com.hiyako.app.ui.screens.LibraryScreen
import com.hiyako.app.ui.screens.ProfileScreen

@Composable
fun HiyakoNavHost(navController: NavHostController, onMenuClick: () -> Unit) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(onMenuClick = onMenuClick) }
        composable(Routes.LIBRARY) { LibraryScreen() }
        composable(Routes.PROFILE) { ProfileScreen() }
    }
}
