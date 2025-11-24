package com.example.germanlearningapp.domain.usecase

import com.example.germanlearningapp.domain.model.Card
import com.example.germanlearningapp.domain.model.StudyMode

class GetNextCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(
        studyMode: StudyMode,
        newCardLimit: Int,
        newCardsStudiedToday: Int
    ): Card? {
        val now = System.currentTimeMillis()
        
        // 1. Get due reviews
        val dueCards = cardRepository.getDueCards(now)
        
        // 2. Get new cards if allowed
        val newCardsAllowed = newCardLimit - newCardsStudiedToday
        val newCards = if (newCardsAllowed > 0) {
            cardRepository.getNewCards(newCardsAllowed)
        } else {
            emptyList()
        }

        // 3. Decide based on mode
        return when (studyMode) {
            StudyMode.REVIEW_ONLY -> dueCards.firstOrNull()
            StudyMode.NEW_THEN_REVIEW -> newCards.firstOrNull() ?: dueCards.firstOrNull()
            StudyMode.MIXED -> {
                // Simple mix strategy: interleave or prioritize reviews slightly
                if (dueCards.isNotEmpty()) dueCards.first()
                else newCards.firstOrNull()
            }
        }
    }
}
