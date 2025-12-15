package com.example.germanlearningapp.data.repository

import com.example.germanlearningapp.data.local.dao.CardDao
import com.example.germanlearningapp.data.local.dao.DeckDao
import com.example.germanlearningapp.data.local.dao.ReviewStateDao
import com.example.germanlearningapp.data.local.entity.CardEntity
import com.example.germanlearningapp.data.local.entity.DeckEntity
import com.example.germanlearningapp.data.local.entity.ReviewStateEntity
import com.example.germanlearningapp.domain.model.Card
import com.example.germanlearningapp.domain.model.CardType
import com.example.germanlearningapp.domain.model.Deck
import com.example.germanlearningapp.domain.model.ReviewState
import com.example.germanlearningapp.domain.usecase.CardRepository

class CardRepositoryImpl(
    private val cardDao: CardDao,
    private val deckDao: DeckDao,
    private val reviewStateDao: ReviewStateDao
) : CardRepository {

    override suspend fun getDecks(): List<Deck> {
        return deckDao.getAllDecks().map { it.toDomain() }
    }

    override suspend fun getCardsForDeck(deckId: Long): List<Card> {
        return cardDao.getCardsByDeck(deckId).map { it.toDomain() }
    }

    override suspend fun getDueCards(now: Long, deckId: Long): List<Card> {
        val dueReviews = reviewStateDao.getDue(now)
        if (dueReviews.isEmpty()) return emptyList()
        val cardIds = dueReviews.map { it.cardId }
        return cardDao.getCardsByIds(cardIds)
            .filter { it.deckId == deckId }
            .map { it.toDomain() }
    }

    override suspend fun getNewCards(limit: Int, deckId: Long): List<Card> {
        // Fetch extra to allow filtering
        return cardDao.getNewCards(limit * 2)
            .filter { it.deckId == deckId }
            .take(limit)
            .map { it.toDomain() }
    }

    override suspend fun saveReviewState(state: ReviewState) {
        reviewStateDao.upsert(state.toEntity())
    }

    override suspend fun getReviewState(cardId: Long): ReviewState? {
        return reviewStateDao.getReviewState(cardId)?.toDomain()
    }

    override suspend fun resetAllDueDates() {
        reviewStateDao.resetAllDueDates()
    }

    override suspend fun ensureDataSeeded() {
        if (deckDao.getAllDecks().isEmpty()) {
            // Use explicit IDs to avoid duplicates if this runs multiple times
            val deck1Id = deckDao.insertDeck(DeckEntity(id = 1, name = "Basic Vocabulary", description = "Common words for beginners", level = 1))
            val deck2Id = deckDao.insertDeck(DeckEntity(id = 2, name = "Accusative Case", description = "Learn the accusative case", level = 2))
            val deck3Id = deckDao.insertDeck(DeckEntity(id = 3, name = "Travel Sentences", description = "Useful phrases for traveling", level = 3))

            // Seed Cards (Deck 1)
            cardDao.insertCards(listOf(
                CardEntity(deckId = deck1Id, frontText = "The Dog", backText = "Der Hund", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "The Cat", backText = "Die Katze", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "The House", backText = "Das Haus", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "The Car", backText = "Das Auto", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "The Apple", backText = "Der Apfel", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "The Woman", backText = "Die Frau", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "The Man", backText = "Der Mann", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "The Child", backText = "Das Kind", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "Good Morning", backText = "Guten Morgen", isMirrorable = true),
                CardEntity(deckId = deck1Id, frontText = "Thank you", backText = "Danke", isMirrorable = true)
            ))

            // Seed Cards (Deck 2)
            cardDao.insertCards(listOf(
                CardEntity(deckId = deck2Id, frontText = "I see the man", backText = "Ich sehe den Mann", isMirrorable = false),
                CardEntity(deckId = deck2Id, frontText = "He has a ball", backText = "Er hat einen Ball", isMirrorable = false)
            ))
        }
    }

    private fun DeckEntity.toDomain() = Deck(
        id = id,
        name = name,
        description = description,
        level = level,
        isPro = false 
    )

    private fun ReviewStateEntity.toDomain() = ReviewState(
        cardId = cardId,
        easeFactor = easeFactor,
        intervalDays = intervalDays,
        repetitions = repetitions,
        lapses = lapses,
        dueDate = dueDate,
        lastReviewed = lastReviewed
    )

    private fun CardEntity.toDomain(): Card {
        return Card(
            id = id,
            deckId = deckId,
            type = CardType.VOCAB, 
            frontText = frontText,
            backText = backText,
            extraData = null, 
            isActive = true,
            isMirrorable = isMirrorable
        )
    }

    private fun ReviewState.toEntity() = ReviewStateEntity(
        cardId = cardId,
        easeFactor = easeFactor,
        intervalDays = intervalDays,
        repetitions = repetitions,
        lapses = lapses,
        dueDate = dueDate,
        lastReviewed = lastReviewed
    )
}
