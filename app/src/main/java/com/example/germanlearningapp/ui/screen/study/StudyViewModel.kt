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
import com.example.germanlearningapp.domain.model.SrsScheduler
import com.example.germanlearningapp.domain.usecase.CardRepository
import com.example.germanlearningapp.utils.TimeFormatter
import kotlinx.coroutines.launch
import java.util.LinkedList

class StudyViewModel(
    private val cardRepository: CardRepository,
    private val getNextCardUseCase: GetNextCardUseCase,
    private val rateCardUseCase: RateCardUseCase
) : ViewModel() {

    var uiState by mutableStateOf(StudyUiState())
        private set

    private val sessionQueue = LinkedList<Card>()

    fun startSession(deckId: Long, studyMode: StudyMode) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            sessionQueue.clear()
            // Fetch a batch of cards for the session
            val dueCards = cardRepository.getDueCards(System.currentTimeMillis(), deckId)
            val newCards = if (studyMode != StudyMode.REVIEW_ONLY) {
                cardRepository.getNewCards(10, deckId)
            } else emptyList()

            val combined = when (studyMode) {
                StudyMode.REVIEW_ONLY -> dueCards
                StudyMode.NEW_ONLY -> newCards
                else -> (dueCards + newCards).shuffled()
            }

            sessionQueue.addAll(combined)
            loadNextCardFromQueue()
        }
    }

    private fun loadNextCardFromQueue() {
        viewModelScope.launch {
            val nextCard = sessionQueue.poll()

            val nextReviewTimes = if (nextCard != null) {
                val reviewState = cardRepository.getReviewState(nextCard.id)
                val now = System.currentTimeMillis()
                mapOf(
                    Rating.AGAIN to SrsScheduler.calculateNextState(reviewState, Rating.AGAIN, now),
                    Rating.GOOD to SrsScheduler.calculateNextState(reviewState, Rating.GOOD, now),
                    Rating.EASY to SrsScheduler.calculateNextState(reviewState, Rating.EASY, now)
                ).mapValues { (_, state) ->
                    TimeFormatter.formatDuration(state.dueDate - now)
                }
            } else {
                emptyMap()
            }
            
            uiState = uiState.copy(
                currentCard = nextCard,
                isLoading = false,
                isAnswerShown = false,
                nextReviewTimes = nextReviewTimes
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
            
            if (rating == Rating.AGAIN && sessionQueue.isNotEmpty()) {
                // Re-add to the end of the queue
                sessionQueue.add(card)
            }
            
            loadNextCardFromQueue()
        }
    }

    companion object {
        fun provideFactory(
            cardRepository: CardRepository,
            getNextCardUseCase: GetNextCardUseCase,
            rateCardUseCase: RateCardUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StudyViewModel(cardRepository, getNextCardUseCase, rateCardUseCase) as T
            }
        }
    }
}

data class StudyUiState(
    val currentCard: Card? = null,
    val isAnswerShown: Boolean = false,
    val isLoading: Boolean = true,
    val isNewCard: Boolean = false, 
    val nextReviewTimes: Map<Rating, String> = emptyMap()
)
