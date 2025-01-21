package com.example.challengetracker.repository

import com.example.challengetracker.dao.ChallengeDao
import com.example.challengetracker.entity.Challenge

class ChallengeRepository(private val challengeDao: ChallengeDao) {
    suspend fun insertChallenge(challenge: Challenge) = challengeDao.insertChallenge(challenge)
    suspend fun getChallengesByUserId(userId: String) = challengeDao.getChallengesByUserId(userId)
    suspend fun updateChallenge(challenge: Challenge) = challengeDao.updateChallenge(challenge)
    suspend fun deleteChallenge(challenge: Challenge) = challengeDao.deleteChallenge(challenge)
    suspend fun deleteChallengeById(challengeid: Int) = challengeDao.deleteChallengeById(challengeid)
    suspend fun getDefaultChallenges()= challengeDao.getDefaultChallenges()
    suspend fun getAllChallenges() {
        challengeDao.getAllChallenges()
    }
}
