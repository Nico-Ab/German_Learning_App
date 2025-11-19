package com.example.germanlearningapp.ui.screen.home

data class HomeUiState(
    val dueCount: Int = 0,
    val newCount: Int = 0,
    val streak: Int = 0,
    val isLoading: Boolean = true
)