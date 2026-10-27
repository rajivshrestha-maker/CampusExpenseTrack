package com.example.campusexpensetrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val studentId: String,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String
)
