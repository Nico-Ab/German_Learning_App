package com.example.germanlearningapp.domain.model

data class Card(
    val id: Long,
    val deckId: Long,
    val type: CardType = CardType.VOCAB, // Default to avoid breaking changes
    val frontText: String,       // main German text or sentence
    val backText: String,        // translation / explanation
    val extraData: String? = null, // JSON or serialized data for options, word bank, etc.
    val isActive: Boolean = true,
    val isMirrorable: Boolean = true // Added field for mirroring capability
)