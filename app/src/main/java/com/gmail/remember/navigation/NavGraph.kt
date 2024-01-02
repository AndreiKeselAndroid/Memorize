package com.gmail.remember.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.gmail.remember.screens.addwords.AddWordsScreen
import com.gmail.remember.screens.auth.AuthScreen
import com.gmail.remember.screens.main.MainScreen
import com.gmail.remember.screens.words.WordsScreen
import com.gmail.remember.screens.profile.ProfileScreen
import com.gmail.remember.screens.trainingwords.TrainingWordsScreen

const val CHILD_NAME = "child_name"
const val WORD = "word"
const val DEEP_LINK_TRAINING = "https://remember.com/training"

@Composable
internal fun NavGraph(
    navController: NavHostController,
    isAuth: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isAuth) Screens.AuthScreen.route else Screens.MainScreen.route
    ) {
        composable(route = Screens.AuthScreen.route) {
            AuthScreen(navController = navController)
        }

        composable(route = Screens.MainScreen.route) {
            MainScreen(navController = navController)
        }

        composable(
            route = Screens.WordsScreen.route,
            arguments = listOf(navArgument(CHILD_NAME) { type = NavType.StringType })
        ) {
            WordsScreen(navController = navController)
        }

        composable(
            route = Screens.AddWordsScreen.route,
            arguments = listOf(navArgument(CHILD_NAME) { type = NavType.StringType })
        ) {
            AddWordsScreen(navController = navController)
        }

        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }

        composable(
            route = Screens.TrainingWordsScreen.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "$DEEP_LINK_TRAINING/{$WORD}"
            })
        ) {
            TrainingWordsScreen(navController = navController)
        }
    }
}

fun NavHostController.navigateSafeArgs(
    route: String,
    args: String?,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    this.navigate("${route.substringBefore("/")}/${args}") {
        builder()
    }
}