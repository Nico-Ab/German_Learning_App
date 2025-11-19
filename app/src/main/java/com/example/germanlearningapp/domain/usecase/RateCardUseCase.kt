package com.example.germanlearningapp.domain.usecase

import android.media.Rating

class RateCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(cardId: Long, rating: Rating) {
        // update SRS state using domain.srs logic
    }
}