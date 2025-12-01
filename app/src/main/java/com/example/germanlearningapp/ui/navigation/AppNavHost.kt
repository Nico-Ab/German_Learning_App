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
import com.example.germanlearningapp.ui.screen.decks.DecksScreen
import com.example.germanlearningapp.ui.screen.home.HomeScreen
import com.example.germanlearningapp.ui.screen.study.StudyScreen
import com.example.germanlearningapp.ui.screen.progress.ProgressScreen
import com.example.germanlearningapp.ui.screen.pro.ProScreen
import com.example.germanlearningapp.ui.screen.settings.SettingsScreen

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
                navArgument("deckId") { type = NavType.LongType },
                navArgument("mode") { type = NavType.StringType; defaultValue = "MIXED" }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: 0L
            val mode = backStackEntry.arguments?.getString("mode") ?: "MIXED"
            StudyScreen(
                deckId = deckId,
                mode = mode,
                navController = navController
            )
        }
    }
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
