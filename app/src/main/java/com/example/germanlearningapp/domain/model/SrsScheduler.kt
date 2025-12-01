package com.example.germanlearningapp.domain.model

import kotlin.math.max
import kotlin.math.roundToInt

object SrsScheduler {
    private const val MIN_EASE = 1.3
    private const val START_EASE = 2.5

    // Calculate next review state
    fun calculateNextState(
        currentState: ReviewState?,
        rating: Rating,
        now: Long
    ): ReviewState {
        if (currentState == null) {
            return calculateInitialReview(rating, now)
        }
        return calculateReview(currentState, rating, now)
    }

    // Initial review
    private fun calculateInitialReview(rating: Rating, now: Long): ReviewState {
        val easeFactor = START_EASE
        val (intervalDays, repetitions) = when (rating) {
            Rating.AGAIN -> 0 to 0
            Rating.GOOD -> 1 to 1
            Rating.EASY -> 4 to 1
        }

        return ReviewState(
            cardId = 0,
            easeFactor = easeFactor,
            intervalDays = intervalDays,
            repetitions = repetitions,
            lapses = 0,
            dueDate = now + (intervalDays * 24 * 60 * 60 * 1000L),
            lastReviewed = now
        )
    }

    // Subsequent review
    private fun calculateReview(current: ReviewState, rating: Rating, now: Long): ReviewState {
        if (rating == Rating.AGAIN) {
            return current.copy(
                intervalDays = 0,
                repetitions = 0,
                lapses = current.lapses + 1,
                dueDate = now,
                lastReviewed = now,
                easeFactor = max(MIN_EASE, current.easeFactor - 0.2)
            )
        }

        var newEase = current.easeFactor + (0.1 - (5 - ratingScore(rating)) * (0.08 + (5 - ratingScore(rating)) * 0.02))
        if (newEase < MIN_EASE) newEase = MIN_EASE

        val newInterval = if (current.repetitions == 0) {
            1
        } else if (current.repetitions == 1) {
            6
        } else {
            (current.intervalDays * newEase).roundToInt()
        }
        
        val bonus = if (rating == Rating.EASY) 1.3 else 1.0
        val finalInterval = (newInterval * bonus).roundToInt()

        return current.copy(
            intervalDays = finalInterval,
            repetitions = current.repetitions + 1,
            easeFactor = newEase,
            dueDate = now + (finalInterval * 24 * 60 * 60 * 1000L),
            lastReviewed = now
        )
    }

    private fun ratingScore(rating: Rating): Int {
        return when (rating) {
            Rating.AGAIN -> 0 // Should not happen
            Rating.GOOD -> 4
            Rating.EASY -> 5
        }
    }
}
