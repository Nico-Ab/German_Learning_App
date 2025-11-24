package com.example.germanlearningapp.domain.usecase

import com.example.germanlearningapp.data.local.dao.ReviewStateDao
import com.example.germanlearningapp.domain.model.Rating
import com.example.germanlearningapp.domain.model.SrsScheduler

class RateCardUseCase(
    private val reviewStateDao: ReviewStateDao,
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(cardId: Long, rating: Rating) {
        val now = System.currentTimeMillis()
        
        // 1. Fetch existing state
        val currentStateEntity = reviewStateDao.getReviewState(cardId)
        
        // Map entity to domain model if it exists, else null
        val currentState = currentStateEntity?.let {
            com.example.germanlearningapp.domain.model.ReviewState(
                cardId = it.cardId,
                easeFactor = it.easeFactor,
                intervalDays = it.intervalDays,
                repetitions = it.repetitions,
                lapses = it.lapses,
                dueDate = it.dueDate,
                lastReviewed = it.lastReviewed
            )
        }

        // 2. Calculate new state
        val newState = SrsScheduler.calculateNextState(currentState, rating, now).copy(cardId = cardId)

        // 3. Save
        cardRepository.saveReviewState(newState)
    }
}
