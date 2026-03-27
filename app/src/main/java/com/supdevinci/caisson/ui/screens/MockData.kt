package com.supdevinci.caisson.ui.screens

import com.supdevinci.caisson.data.local.entities.CocktailEntity
import java.util.Date

object MockData {
    val cocktails = listOf(
        CocktailEntity(
            id = "1",
            name = "Mojito",
            instructions = "Muddle mint leaves with sugar and lime juice. Add a splash of soda water and fill the glass with cracked ice. Pour the rum and top with soda water.",
            createdAt = Date(),
            isFavorite = true
        ),
        CocktailEntity(
            id = "2",
            name = "Refreshing Green Detox",
            instructions = "Every cocktail has a story. Find yours. Refreshing • 13% ABV",
            createdAt = Date(System.currentTimeMillis() - 86400000), // 1 day ago
            isFavorite = false
        ),
        // Add more mock data as needed
        CocktailEntity(
            id = "3",
            name = "Old Fashioned",
            instructions = "Place sugar cube in old fashioned glass and saturate with bitters, add a dash of plain water. Muddle until dissolved. Fill the glass with ice cubes and add whiskey.",
            createdAt = Date(System.currentTimeMillis() - 172800000), // 2 days ago
            isFavorite = true
        )
    )

    val users = listOf(
        LeaderboardUser(1, "Cocktail King", "California, USA", 1247, 1),
        LeaderboardUser(2, "Mix Master", "London, UK", 1156, 2),
        LeaderboardUser(3, "BarTender Pro", "Paris, France", 1089, 3),
        LeaderboardUser(4, "Drink Expert", "Berlin, Germany", 967, 4),
        LeaderboardUser(5, "Party Legend", "Madrid, Spain", 892, 5)
    )
}

data class LeaderboardUser(
    val id: Int,
    val name: String,
    val location: String,
    val drinks: Int,
    val rank: Int
)
