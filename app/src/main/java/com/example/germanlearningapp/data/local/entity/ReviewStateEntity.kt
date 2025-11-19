package com.example.germanlearningapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_states", primaryKeys = ["cardId"])
data class ReviewStateEntity(
    val cardId: Long,
    val easeFactor: Double,
    val intervalDays: Int,
    val repetitions: Int,
    val lapses: Int,
    val dueDate: Long,
    val lastReviewed: Long?
)