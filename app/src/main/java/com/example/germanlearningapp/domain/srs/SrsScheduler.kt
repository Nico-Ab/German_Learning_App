package com.example.germanlearningapp.domain.srs

import android.media.Rating
import com.example.germanlearningapp.domain.model.ReviewState

object SrsScheduler {

    fun updateState(
        current: ReviewState?,
        rating: Rating,
        now: Long
    ): ReviewState {
        // if current == null -> first review
        // adjust easeFactor, intervalDays, repetitions, lapses
        // compute next dueDate = now + intervalDays
        TODO("Provide the return value")
    }
}