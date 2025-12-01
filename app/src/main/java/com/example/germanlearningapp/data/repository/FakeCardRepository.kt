package com.example.germanlearningapp.data.repository

import com.example.germanlearningapp.domain.model.Card
import com.example.germanlearningapp.domain.model.CardType
import com.example.germanlearningapp.domain.model.Deck
import com.example.germanlearningapp.domain.model.ReviewState
import com.example.germanlearningapp.domain.usecase.CardRepository
import kotlinx.coroutines.delay

class FakeCardRepository : CardRepository {
    private val decks = listOf(
        Deck(1, "Basic Vocabulary", "Common words for beginners", 1, false),
        Deck(2, "Accusative Case", "Learn the accusative case", 2, false),
        Deck(3, "Travel Sentences", "Useful phrases for traveling", 3, true)
    )
    // Duplicate cards with swapped languages
    private fun createBilingualCards(baseCards: List<Card>): List<Card> {
        val allCards = mutableListOf<Card>()
        var idCounter = 1000L 

        baseCards.forEach { card ->
            // English front
            val engFront = card.copy(
                id = idCounter++,
                frontText = card.backText, 
                backText = card.frontText, 
                deckId = card.deckId
            )
            allCards.add(engFront)

            // German front
            val gerFront = card.copy(
                id = idCounter++,
                frontText = card.frontText,
                backText = card.backText,
                deckId = card.deckId
            )
            allCards.add(gerFront)
        }
        return allCards
    }

    private val baseCards = listOf(
        Card(1, 1, CardType.VOCAB, "Der Hund", "The Dog", null, true),
        Card(2, 1, CardType.VOCAB, "Die Katze", "The Cat", null, true),
        Card(3, 1, CardType.VOCAB, "Das Haus", "The House", null, true),
        Card(4, 1, CardType.VOCAB, "Das Auto", "The Car", null, true),
        Card(5, 1, CardType.VOCAB, "Der Apfel", "The Apple", null, true),
        Card(6, 1, CardType.VOCAB, "Die Frau", "The Woman", null, true),
        Card(7, 1, CardType.VOCAB, "Der Mann", "The Man", null, true),
        Card(8, 1, CardType.VOCAB, "Das Kind", "The Child", null, true),
        Card(9, 1, CardType.VOCAB, "Guten Morgen", "Good Morning", null, true),
        Card(10, 1, CardType.VOCAB, "Danke", "Thank you", null, true),
        
        Card(11, 2, CardType.CASE, "Ich sehe ___ Mann (the)", "den", null, true),
        Card(12, 2, CardType.CASE, "Er hat ___ Ball (a)", "einen", null, true)
    )

    private val allCards = createBilingualCards(baseCards).shuffled()
    
    // Track what we've seen so far
    private val reviewedCardIds = mutableSetOf<Long>()

    override suspend fun getDecks(): List<Deck> = decks

    override suspend fun getCardsForDeck(deckId: Long): List<Card> {
        return allCards.filter { it.deckId == deckId }
    }

    override suspend fun getDueCards(now: Long, deckId: Long): List<Card> {
        // Filter out already reviewed cards
        return allCards
            .filter { it.deckId == deckId && !reviewedCardIds.contains(it.id) }
            .take(5)
    }

    override suspend fun getNewCards(limit: Int, deckId: Long): List<Card> {
        return allCards
            .filter { it.deckId == deckId && !reviewedCardIds.contains(it.id) }
            .drop(5)
            .take(limit)
    }

    override suspend fun saveReviewState(state: ReviewState) {
        delay(100)
        reviewedCardIds.add(state.cardId)
    }
}
