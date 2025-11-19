package com.example.germanlearningapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deckId: Long,
    val type: String,           // store enum as String
    val frontText: String,
    val backText: String,
    val extraData: String?,
    val isActive: Boolean
)