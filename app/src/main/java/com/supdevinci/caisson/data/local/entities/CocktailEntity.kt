package com.supdevinci.caisson.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.supdevinci.caisson.model.IngredientMeasure
import java.util.Date

@Entity(tableName = "cocktails")
data class CocktailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val instructions: String,
    val imageUrl: String? = null,
    val ingredients: List<IngredientMeasure> = emptyList(),
    val isFavorite: Boolean = false,
    val createdAt: Date = Date(),
    val deletedAt: Date? = null
)
