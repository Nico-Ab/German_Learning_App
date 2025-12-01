package com.example.germanlearningapp.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.germanlearningapp.domain.usecase.CardRepository
import com.example.germanlearningapp.domain.model.Deck
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            // Simulate refresh delay to ensure we fetch fresh data if coming back from another screen
            delay(50) // Optional but helpful in non-reactive flows without flows
            
            val now = System.currentTimeMillis()
            // We should ideally aggregate stats across ALL decks or fetch for a specific one.
            // For now, let's sum up for all decks to show global stats, or pick the last used.
            
            // To make numbers match "clicked through all cards", we need to query the repo again.
            // The FakeRepo tracks reviewedCardIds.
            // If we iterate all decks:
            
            val decks = cardRepository.getDecks()
            var totalDue = 0
            var totalNew = 0
            
            decks.forEach { deck ->
                val due = cardRepository.getDueCards(now, deck.id)
                // Logic in FakeRepo splits cards into Due (first 5) and New (rest).
                // If we review them, they disappear from both lists in FakeRepo.
                
                totalDue += due.size
                
                // Note: FakeRepo's getNewCards drops 5. If less than 5 remain total, it returns empty.
                val new = cardRepository.getNewCards(100, deck.id) 
                totalNew += new.size
            }

            // Update Last Deck logic
            val lastDeck = decks.firstOrNull() // Placeholder

            uiState = uiState.copy(
                reviewsDue = totalDue,
                newCardsAvailable = totalNew,
                lastUsedDeck = lastDeck
            )
        }
    }

    companion object {
        fun provideFactory(repository: CardRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(repository) as T
            }
        }
    }
}
