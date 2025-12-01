package com.example.germanlearningapp.ui.screen.study

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.germanlearningapp.domain.model.Card
import com.example.germanlearningapp.domain.model.Rating
import com.example.germanlearningapp.domain.model.StudyMode
import com.example.germanlearningapp.domain.usecase.GetNextCardUseCase
import com.example.germanlearningapp.domain.usecase.RateCardUseCase
import kotlinx.coroutines.launch

class StudyViewModel(
    private val getNextCardUseCase: GetNextCardUseCase,
    private val rateCardUseCase: RateCardUseCase
) : ViewModel() {

    var uiState by mutableStateOf(StudyUiState())
        private set

    private var newCardsStudiedToday = 0
    private val dailyNewLimit = 10 // TODO: Get from settings

    fun loadNextCard(deckId: Long, studyMode: StudyMode = StudyMode.MIXED) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val card = getNextCardUseCase(
                deckId = deckId,
                studyMode = studyMode,
                newCardLimit = dailyNewLimit,
                newCardsStudiedToday = newCardsStudiedToday
            )
            
            uiState = uiState.copy(
                currentCard = card,
                isLoading = false,
                isAnswerShown = false,
                isNewCard = true // Placeholder: Logic to determine if card is new needs repo query
            )
        }
    }

    fun showAnswer() {
        uiState = uiState.copy(isAnswerShown = true)
    }

    fun rateCard(rating: Rating) {
        val card = uiState.currentCard ?: return
        viewModelScope.launch {
            rateCardUseCase(card.id, rating)
            loadNextCard(card.deckId)
        }
    }

    companion object {
        fun provideFactory(
            getNextCardUseCase: GetNextCardUseCase,
            rateCardUseCase: RateCardUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StudyViewModel(getNextCardUseCase, rateCardUseCase) as T
            }
        }
    }
}

data class StudyUiState(
    val currentCard: Card? = null,
    val isAnswerShown: Boolean = false,
    val isLoading: Boolean = true,
    val isNewCard: Boolean = false
)
