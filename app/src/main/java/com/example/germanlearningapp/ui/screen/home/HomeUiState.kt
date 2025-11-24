package com.example.germanlearningapp.ui.screen.home

import com.example.germanlearningapp.domain.model.Deck

data class HomeUiState(
    val reviewsDue: Int = 0,
    val newCardsAvailable: Int = 0,
    val lastUsedDeck: Deck? = null
)