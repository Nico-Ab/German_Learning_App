package com.example.germanlearningapp.domain.usecase

import com.example.germanlearningapp.domain.model.Card
import com.example.germanlearningapp.domain.model.Deck
import com.example.germanlearningapp.domain.model.ReviewState

interface CardRepository {
    suspend fun getDecks(): List<Deck>
    suspend fun getCardsForDeck(deckId: Long): List<Card>
    suspend fun getDueCards(now: Long): List<Card>
    suspend fun getNewCards(limit: Int): List<Card>
    suspend fun saveReviewState(state: ReviewState)
}