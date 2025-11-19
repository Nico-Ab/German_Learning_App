package com.example.germanlearningapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.germanlearningapp.data.local.dao.CardDao
import com.example.germanlearningapp.data.local.dao.DeckDao
import com.example.germanlearningapp.data.local.dao.ReviewStateDao
import com.example.germanlearningapp.data.local.entity.ReviewStateEntity
import com.example.germanlearningapp.data.local.entity.CardEntity
import com.example.germanlearningapp.data.local.entity.DeckEntity

@Database(
    entities = [CardEntity::class, DeckEntity::class, ReviewStateEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun reviewStateDao(): ReviewStateDao
    abstract fun deckDao(): DeckDao
}