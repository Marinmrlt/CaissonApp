package com.supdevinci.caisson.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "drink_logs")
data class DrinkLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cocktailId: String,
    val cocktailName: String,
    val abvEstimated: Float,
    val volumeMl: Int,
    val timestamp: Date = Date()
)
