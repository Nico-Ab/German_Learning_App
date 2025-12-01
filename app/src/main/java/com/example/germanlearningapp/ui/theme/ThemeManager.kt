package com.example.germanlearningapp.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeManager {
    var isDarkTheme by mutableStateOf(false) // Default to Light or System
    
    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}
