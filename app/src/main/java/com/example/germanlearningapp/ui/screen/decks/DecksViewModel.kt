package com.example.germanlearningapp.ui.screen.decks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.germanlearningapp.domain.model.Deck
import com.example.germanlearningapp.domain.usecase.CardRepository
import kotlinx.coroutines.launch

class DecksViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    var uiState by mutableStateOf(DecksUiState())
        private set

    init {
        loadDecks()
    }

    private fun loadDecks() {
        viewModelScope.launch {
            val decks = cardRepository.getDecks()
            uiState = uiState.copy(
                decks = decks,
                isLoading = false
            )
        }
    }

    companion object {
        fun provideFactory(repository: CardRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DecksViewModel(repository) as T
            }
        }
    }
}

data class DecksUiState(
    val decks: List<Deck> = emptyList(),
    val isLoading: Boolean = true
)
