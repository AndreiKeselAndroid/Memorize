package com.gmail.remember.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gmail.remember.screens.addwords.AddWordsScreen
import com.gmail.remember.screens.auth.AuthScreen
import com.gmail.remember.screens.remember.RememberScreen
import com.gmail.remember.screens.profile.ProfileScreen

@Composable
internal fun NavGraph(
    navController: NavHostController,
    isAuth: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isAuth) Screens.AuthScreen.route else Screens.RememberScreen.route
    ) {
        composable(route = Screens.AuthScreen.route) {
            AuthScreen(navController = navController,)
        }

        composable(route = Screens.RememberScreen.route) {
            RememberScreen(navController = navController)
        }

        composable(route = Screens.AddWordsScreen.route) {
            AddWordsScreen(navController = navController)
        }

        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
    }
}