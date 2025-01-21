package com.example.challengetracker.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "challenges",
)
data class Challenge(
    @PrimaryKey(autoGenerate = true) val challengeId: Int = 0,
    val userId: Int,
    val title: String,
    val description: String,
    val duration: Int,
    var progress: Int,
    val type: String,
    var isCompleted: Boolean = false
)

