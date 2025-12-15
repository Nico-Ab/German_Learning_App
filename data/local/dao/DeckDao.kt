package com.example.germanlearningapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.germanlearningapp.data.local.entity.DeckEntity

@Dao
interface DeckDao {
    @Query("SELECT DISTINCT * FROM decks")
    suspend fun getAllDecks(): List<DeckEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: DeckEntity): Long 
}