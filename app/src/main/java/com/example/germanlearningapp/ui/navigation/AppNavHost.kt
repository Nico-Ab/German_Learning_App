package com.example.germanlearningapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Decks.route) {
            DecksScreen(navController = navController)
        }

        composable(Screen.Progress.route) {
            ProgressScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.Pro.route) {
            ProScreen(navController = navController)
        }

        composable(
            route = Screen.Study.route,
            arguments = listOf(
                navArgument("deckId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: 0L
            StudyScreen(
                deckId = deckId,
                navController = navController
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    PlaceholderScreen(text = "Home Screen")
}

@Composable
fun DecksScreen(navController: NavHostController) {
    PlaceholderScreen(text = "Decks Screen")
}

@Composable
fun ProgressScreen() {
    PlaceholderScreen(text = "Progress Screen")
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    PlaceholderScreen(text = "Settings Screen")
}

@Composable
fun ProScreen(navController: NavHostController) {
    PlaceholderScreen(text = "Pro Screen")
}

@Composable
fun StudyScreen(deckId: Long, navController: NavHostController) {
    PlaceholderScreen(text = "Study Screen (Deck: $deckId)")
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}
