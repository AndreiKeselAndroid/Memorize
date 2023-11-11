package com.gmail.remember.navigation

internal sealed class Screens(val route: String) {
    data object AuthScreen : Screens("auth_screen")
    data object RememberScreen : Screens("remember_screen")
    data object SettingsScreen : Screens("setting_screen")
    data object AddWordsScreen : Screens("add_words_screen")
}