package com.example.campusexpensetrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val studentId: String,
    val fullName: String,
    val monthlyBudget: Double
)
