package com.example.germanlearningapp.domain.model

data class Deck(
    val id: Long,
    val name: String,
    val description: String,
    val level: Int,              // 1, 2, 3
    val isPro: Boolean           // for monetization later
)