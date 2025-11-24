package com.example.germanlearningapp.di

import com.example.germanlearningapp.data.repository.FakeCardRepository
import com.example.germanlearningapp.data.local.dao.ReviewStateDao
import com.example.germanlearningapp.data.local.entity.ReviewStateEntity
import com.example.germanlearningapp.domain.usecase.CardRepository
import com.example.germanlearningapp.domain.usecase.GetNextCardUseCase
import com.example.germanlearningapp.domain.usecase.RateCardUseCase

object ServiceLocator {
    val cardRepository: CardRepository by lazy {
        FakeCardRepository()
    }

    // We need a fake ReviewStateDao for RateCardUseCase since FakeRepository doesn't use Room
    // Ideally RateCardUseCase should use Repository only, but currently it uses Dao directly.
    // I'll make a quick fake implementation here or refactor RateCardUseCase.
    // Refactoring RateCardUseCase to use Repository is better but for now let's mock the DAO.
    
    private val fakeReviewStateDao = object : ReviewStateDao {
        override suspend fun getReviewState(cardId: Long): ReviewStateEntity? = null
        override suspend fun upsert(reviewState: ReviewStateEntity) {}
        override suspend fun getDue(now: Long): List<ReviewStateEntity> = emptyList()
    }

    val getNextCardUseCase: GetNextCardUseCase by lazy {
        GetNextCardUseCase(cardRepository)
    }

    val rateCardUseCase: RateCardUseCase by lazy {
        RateCardUseCase(fakeReviewStateDao, cardRepository)
    }
}
