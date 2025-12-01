package com.example.germanlearningapp.domain.usecase

import com.example.germanlearningapp.domain.model.Card
import com.example.germanlearningapp.domain.model.StudyMode

class GetNextCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(
        deckId: Long,
        studyMode: StudyMode,
        newCardLimit: Int,
        newCardsStudiedToday: Int
    ): Card? {
        val now = System.currentTimeMillis()
        
        // Get due cards
        val dueCards = cardRepository.getDueCards(now, deckId)
        
        // Get new cards if allowed
        val newCardsAllowed = newCardLimit - newCardsStudiedToday
        val newCards = if (newCardsAllowed > 0) {
            cardRepository.getNewCards(newCardsAllowed, deckId)
        } else {
            emptyList()
        }

        // Select card based on mode
        return when (studyMode) {
            StudyMode.REVIEW_ONLY -> dueCards.firstOrNull()
            StudyMode.NEW_THEN_REVIEW -> newCards.firstOrNull() ?: dueCards.firstOrNull()
            StudyMode.MIXED -> {
                if (dueCards.isNotEmpty()) dueCards.first()
                else newCards.firstOrNull()
            }
        }
    }
}
