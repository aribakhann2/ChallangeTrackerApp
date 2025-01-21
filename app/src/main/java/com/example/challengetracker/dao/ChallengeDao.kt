package com.example.challengetracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.challengetracker.entity.Challenge

@Dao
interface ChallengeDao {
    @Insert
    suspend fun insertChallenge(challenge: Challenge): Long

    @Query("SELECT * FROM challenges WHERE userId = :userId")
    suspend fun getChallengesByUserId(userId: String): List<Challenge>

    @Update
    suspend fun updateChallenge(challenge: Challenge)

    @Delete
    suspend fun deleteChallenge(challenge: Challenge)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertAll(defaultChallenges: List<Challenge>)

    @Query("SELECT * FROM challenges")
    suspend fun getAllChallenges(): List<Challenge>

    @Query("Select * FROM challenges WHERE userId=0")
    suspend fun getDefaultChallenges():List<Challenge>

    @Query("DELETE FROM challenges WHERE challengeId = :id")
    suspend fun deleteChallengeById(id: Int)

}
