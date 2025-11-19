package com.example.germanlearningapp.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Decks : Screen("decks")
    data object Study : Screen("study/{deckId}") {
        fun createRoute(deckId: Long) = "study/$deckId"
    }
    data object Progress : Screen("progress")
    data object Settings : Screen("settings")
    data object Pro : Screen("pro")
}