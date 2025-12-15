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
            // Ensure data exists
            cardRepository.ensureDataSeeded()
            
            delay(50) 
            
            val now = System.currentTimeMillis()
            
            val decks = cardRepository.getDecks()
            var totalDue = 0
            var totalNew = 0
            
            decks.forEach { deck ->
                val due = cardRepository.getDueCards(now, deck.id)
                val new = cardRepository.getNewCards(100, deck.id)
                
                totalDue += due.size
                totalNew += new.size
            }
            
            // Refined selection:
            // First deck with Due cards
            val deckWithDue = decks.firstOrNull { 
                cardRepository.getDueCards(now, it.id).isNotEmpty() 
            }
            
            // Or first deck with New cards
            val deckWithNew = decks.firstOrNull {
                cardRepository.getNewCards(1, it.id).isNotEmpty()
            }

            val targetDeck = deckWithDue ?: deckWithNew ?: decks.firstOrNull()

            uiState = uiState.copy(
                reviewsDue = totalDue,
                newCardsAvailable = totalNew,
                lastUsedDeck = targetDeck
            )
        }
    }

    fun resetDueDates() {
        viewModelScope.launch {
            cardRepository.resetAllDueDates()
            loadHomeData() 
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
