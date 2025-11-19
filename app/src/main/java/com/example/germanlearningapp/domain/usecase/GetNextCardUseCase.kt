package com.example.germanlearningapp.domain.usecase

import com.example.germanlearningapp.domain.model.Card
import com.example.germanlearningapp.domain.model.StudyMode

class GetNextCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(
        mode: StudyMode,
        now: Long,
        deckId: Long?
    ): Card? {
        // implement scheduler logic here
        TODO("Provide the return value")
    }
}