package com.example.germanlearningapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.germanlearningapp.data.local.entity.CardEntity

@Dao
interface CardDao {
    @Query("SELECT * FROM cards WHERE deckId = :deckId AND isActive = 1")
    suspend fun getCardsByDeck(deckId: Long): List<CardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>)

    @Query("SELECT * FROM cards WHERE id IN (:ids)")
    suspend fun getCardsByIds(ids: List<Long>): List<CardEntity>

    @Query("SELECT * FROM cards WHERE id NOT IN (SELECT cardId FROM review_states) AND isActive = 1 LIMIT :limit")
    suspend fun getNewCards(limit: Int): List<CardEntity>
}