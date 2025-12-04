package com.example.germanlearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.germanlearningapp.di.ServiceLocator
import com.example.germanlearningapp.ui.navigation.AppNavHost
import com.example.germanlearningapp.ui.theme.GermanLearningAppTheme
import com.example.germanlearningapp.ui.theme.ThemeManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize the repository with the application context
        ServiceLocator.provideCardRepository(applicationContext)
        
        setContent {
            GermanLearningAppTheme(darkTheme = ThemeManager.isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}
