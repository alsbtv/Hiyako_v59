package com.hiyako.app.ui.navigation

import androidx.navigation.NavHostController

object Routes {
    const val HOME = "home"
    const val MYLIST = "mylist"
    const val LIBRARY = "library"
    const val PROFILE = "profile"
    const val DETAILS = "details/{id}"

    fun details(id: String) = "details/$id"
}

fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
