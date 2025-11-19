package com.example.germanlearningapp.ui.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.germanlearningapp.domain.usecase.CardRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue

class HomeViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadTodayStats()
    }

    private fun loadTodayStats() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val due = cardRepository.getDueCards(now)
            // compute newCount from repo (or config)
            uiState = uiState.copy(
                dueCount = due.size,
                newCount = 10, // placeholder
                isLoading = false
            )
        }
    }
}
