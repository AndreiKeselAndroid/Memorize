package com.gmail.remember.navigation

internal sealed class Screens(val route: String) {
    data object AuthScreen : Screens("auth_screen")
    data object WordsScreen : Screens("words_screen/{child_name}")
    data object MainScreen : Screens("main_screen")
    data object ProfileScreen : Screens("profile_screen")
    data object AddWordsScreen : Screens("add_words_screen/{child_name}")
}