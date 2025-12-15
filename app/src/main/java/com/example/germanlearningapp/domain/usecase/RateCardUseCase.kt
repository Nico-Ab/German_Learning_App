package com.example.germanlearningapp.domain.usecase

import com.example.germanlearningapp.data.local.dao.ReviewStateDao
import com.example.germanlearningapp.domain.model.Rating
import com.example.germanlearningapp.domain.model.ReviewState
import com.example.germanlearningapp.domain.model.SrsScheduler

class RateCardUseCase(
    private val reviewStateDao: ReviewStateDao,
    private val cardRepository: CardRepository
) {
    // This now returns the updated review state
    suspend operator fun invoke(cardId: Long, rating: Rating): ReviewState {
        val now = System.currentTimeMillis()
        
        val currentStateEntity = reviewStateDao.getReviewState(cardId)
        
        val currentState = currentStateEntity?.let {
            ReviewState(
                cardId = it.cardId,
                easeFactor = it.easeFactor,
                intervalDays = it.intervalDays,
                repetitions = it.repetitions,
                lapses = it.lapses,
                dueDate = it.dueDate,
                lastReviewed = it.lastReviewed
            )
        }

        val newState = SrsScheduler.calculateNextState(currentState, rating, now).copy(cardId = cardId)

        cardRepository.saveReviewState(newState)
        return newState
    }
}