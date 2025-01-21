package com.example.challengetracker.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.challengetracker.R
import com.example.challengetracker.dao.ChallengeDao
import com.example.challengetracker.entity.Challenge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Challenge::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "challenge_tracker_db"
                )
                    .build()

                INSTANCE = instance
                // Insert default challenges if they don't already exist
                scope.launch(Dispatchers.IO) {
                    populateDefaultChallenges(instance.challengeDao())
                }

                instance
            }
        }

        suspend fun populateDefaultChallenges(challengeDao: ChallengeDao) {
            // Default challenges
            val defaultChallenges = listOf(
                Challenge(userId = 0, title = "Daily Exercise", description = "30 minutes of exercise", duration = 7, progress = 0,type="Fitness" ),
                Challenge(userId = 0, title = "Drink Water", description = "8 glasses of water daily", duration = 7, progress = 0,type="Health"),
                Challenge(userId = 0, title = "Read a Book", description = "Read 10 pages every day", duration = 10, progress = 0,type= "Learn"),
                Challenge(userId = 0, title = "Wake Up Early", description = "Wake up by 6 AM", duration = 14, progress = 0,type="Health"),
                Challenge(userId = 0, title = "Meditation", description = "10 minutes of meditation daily", duration = 7, progress = 0,type="Meditation")
            )
            // Insert default challenges only if they don't already exist
            val existingChallenges = challengeDao.getDefaultChallenges()

            // Assuming this method retrieves all challenges
            var count= existingChallenges.count()
            Log.d("Database", "Default challenges in db ${count}")
            Log.d("Database", "Retrieved challenges from database: $existingChallenges")
            if (existingChallenges.isEmpty()) {
                Log.d("Database", "Populating default challenges: $defaultChallenges")
                challengeDao.insertAll(defaultChallenges)
                Log.d("Database", "Default challenges inserted into the database")
                var newloaded=challengeDao.getDefaultChallenges().count()
                Log.d("Database", "Default challenges inserted into db are: ${newloaded}")
            }
        }
    }
}
