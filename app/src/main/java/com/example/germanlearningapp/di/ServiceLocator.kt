package com.example.germanlearningapp.di

import android.content.Context
import com.example.germanlearningapp.data.local.db.AppDatabase
import com.example.germanlearningapp.data.repository.CardRepositoryImpl
import com.example.germanlearningapp.domain.usecase.CardRepository
import com.example.germanlearningapp.domain.usecase.GetNextCardUseCase
import com.example.germanlearningapp.domain.usecase.RateCardUseCase

object ServiceLocator {

    private var database: AppDatabase? = null
    
    @Volatile
    var cardRepository: CardRepository? = null
        private set

    fun provideCardRepository(context: Context): CardRepository {
        synchronized(this) {
            if (cardRepository == null) {
                val db = AppDatabase.getDatabase(context)
                database = db
                cardRepository = CardRepositoryImpl(db.cardDao(), db.deckDao(), db.reviewStateDao())
            }
            return cardRepository!!
        }
    }

    val getNextCardUseCase: GetNextCardUseCase
        get() = GetNextCardUseCase(cardRepository ?: throw IllegalStateException("Repository not initialized"))

    val rateCardUseCase: RateCardUseCase
        get() {
            val db = database ?: throw IllegalStateException("Database not initialized")
            val repo = cardRepository ?: throw IllegalStateException("Repository not initialized")
            return RateCardUseCase(db.reviewStateDao(), repo)
        }
}
