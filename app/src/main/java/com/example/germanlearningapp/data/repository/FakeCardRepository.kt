package com.example.germanlearningapp.data.repository

import com.example.germanlearningapp.domain.model.Card
import com.example.germanlearningapp.domain.model.CardType
import com.example.germanlearningapp.domain.model.Deck
import com.example.germanlearningapp.domain.model.ReviewState
import com.example.germanlearningapp.domain.usecase.CardRepository

class FakeCardRepository : CardRepository {
    private val decks = listOf(
        Deck(1, "Basic Vocabulary", "Common words for beginners", 1, false),
        Deck(2, "Accusative Case", "Learn the accusative case", 2, false),
        Deck(3, "Travel Sentences", "Useful phrases for traveling", 3, true)
    )

    private val cards = listOf(
        Card(1, 1, CardType.VOCAB, "Der Hund", "The Dog", null, true),
        Card(2, 1, CardType.VOCAB, "Die Katze", "The Cat", null, true),
        Card(3, 1, CardType.VOCAB, "Das Haus", "The House", null, true),
        Card(4, 2, CardType.CASE, "Ich sehe ___ Mann (the)", "den", null, true)
    )

    override suspend fun getDecks(): List<Deck> = decks

    override suspend fun getCardsForDeck(deckId: Long): List<Card> {
        return cards.filter { it.deckId == deckId }
    }

    override suspend fun getDueCards(now: Long): List<Card> {
        // Return a subset for demo
        return cards.take(2)
    }

    override suspend fun getNewCards(limit: Int): List<Card> {
        // Return others
        return cards.drop(2)
    }

    override suspend fun saveReviewState(state: ReviewState) {
        // No-op for fake
    }
}
