package com.example.germanlearningapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.germanlearningapp.data.local.dao.CardDao
import com.example.germanlearningapp.data.local.dao.DeckDao
import com.example.germanlearningapp.data.local.dao.ReviewStateDao
import com.example.germanlearningapp.data.local.entity.CardEntity
import com.example.germanlearningapp.data.local.entity.DeckEntity
import com.example.germanlearningapp.data.local.entity.ReviewStateEntity

@Database(
    entities = [DeckEntity::class, CardEntity::class, ReviewStateEntity::class],
    version = 5, // Increment version to wipe old duplicate data
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
    abstract fun reviewStateDao(): ReviewStateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "german_learning_app_db"
                )
                .fallbackToDestructiveMigration() // Wipe DB on version change
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
