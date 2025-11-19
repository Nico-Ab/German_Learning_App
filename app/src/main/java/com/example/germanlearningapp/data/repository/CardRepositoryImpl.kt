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

    override suspend fun getDueCards(now: Long): List<Card> {
        val dueReviews = reviewStateDao.getDue(now)
        if (dueReviews.isEmpty()) return emptyList()
        val cardIds = dueReviews.map { it.cardId }
        return cardDao.getCardsByIds(cardIds).map { it.toDomain() }
    }

    override suspend fun getNewCards(limit: Int): List<Card> {
        return cardDao.getNewCards(limit).map { it.toDomain() }
    }

    override suspend fun saveReviewState(state: ReviewState) {
        reviewStateDao.upsert(state.toEntity())
    }

    private fun DeckEntity.toDomain() = Deck(
        id = id,
        name = name,
        description = description,
        level = level,
        isPro = isPro
    )

    private fun CardEntity.toDomain(): Card {
        return Card(
            id = id,
            deckId = deckId,
            type = try {
                CardType.valueOf(type)
            } catch (e: IllegalArgumentException) {
                CardType.VOCAB // Fallback or handle error
            },
            frontText = frontText,
            backText = backText,
            extraData = extraData,
            isActive = isActive
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
