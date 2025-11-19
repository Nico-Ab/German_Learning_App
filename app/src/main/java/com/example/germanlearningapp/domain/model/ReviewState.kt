package com.example.germanlearningapp.domain.model

data class ReviewState(
    val cardId: Long,
    val easeFactor: Double,
    val intervalDays: Int,
    val repetitions: Int,
    val lapses: Int,
    val dueDate: Long,           // epoch millis
    val lastReviewed: Long? = null
)