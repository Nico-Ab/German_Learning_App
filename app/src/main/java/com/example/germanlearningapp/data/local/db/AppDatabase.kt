package com.example.germanlearningapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.germanlearningapp.data.local.dao.CardDao
import com.example.germanlearningapp.data.local.dao.DeckDao
import com.example.germanlearningapp.data.local.dao.ReviewStateDao
import com.example.germanlearningapp.data.local.entity.CardEntity
import com.example.germanlearningapp.data.local.entity.DeckEntity
import com.example.germanlearningapp.data.local.entity.ReviewStateEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [DeckEntity::class, CardEntity::class, ReviewStateEntity::class],
    version = 1
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
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Pre-populates the database on first run
private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabase.getDatabase(context)
            val deckDao = database.deckDao()
            val cardDao = database.cardDao()

            // --- Seed Decks ---
            val deck1Id = deckDao.insertDeck(DeckEntity(name = "Basic Vocabulary", description = "Common words for beginners", level = 1))
            val deck2Id = deckDao.insertDeck(DeckEntity(name = "Accusative Case", description = "Learn the accusative case", level = 2))

            // --- Seed Cards ---
            cardDao.insertCards(listOf(
                // Deck 1: Basic Vocab
                CardEntity(deckId = deck1Id, frontText = "The Dog", backText = "Der Hund"),
                CardEntity(deckId = deck1Id, frontText = "The Cat", backText = "Die Katze"),
                CardEntity(deckId = deck1Id, frontText = "The House", backText = "Das Haus"),
                CardEntity(deckId = deck1Id, frontText = "Good Morning", backText = "Guten Morgen"),

                // Deck 2: Case Practice (Not mirrorable)
                CardEntity(deckId = deck2Id, frontText = "I see the man", backText = "Ich sehe den Mann", isMirrorable = false),
                CardEntity(deckId = deck2Id, frontText = "He has a ball", backText = "Er hat einen Ball", isMirrorable = false)
            ))
        }
    }
}
