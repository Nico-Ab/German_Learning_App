package com.example.germanlearningapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.germanlearningapp.data.local.entity.ReviewStateEntity

@Dao
interface ReviewStateDao {
    @Query("SELECT * FROM review_states WHERE cardId = :cardId")
    suspend fun getReviewState(cardId: Long): ReviewStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reviewState: ReviewStateEntity)

    @Query("SELECT * FROM review_states WHERE dueDate <= :now")
    suspend fun getDue(now: Long): List<ReviewStateEntity>
}