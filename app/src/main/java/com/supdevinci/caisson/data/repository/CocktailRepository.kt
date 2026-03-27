package com.supdevinci.caisson.data.repository

import android.util.Log
import com.supdevinci.caisson.data.RetrofitInstance
import com.supdevinci.caisson.data.local.dao.CocktailDao
import com.supdevinci.caisson.data.local.entities.CocktailEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

class CocktailRepository(private val dao: CocktailDao) {
    
    // Always serve UI from the local database
    val allCocktails: Flow<List<CocktailEntity>> = dao.getAllCocktails()
    
    suspend fun syncFromApiIfEmpty() {
        try {
            val count = dao.getAllCocktailsSync().size
            if (count == 0) {
                Log.d("CocktailRepo", "Database is empty. Syncing from TheCocktailDB API...")
                
                // Let's fetch popular ones starting with A, B, C to get a nice chunk of data quickly.
                val searchQueries = listOf("a", "b", "c")
                val allEntities = mutableListOf<CocktailEntity>()
                
                searchQueries.forEach { q ->
                    val response = RetrofitInstance.api.searchCocktails(q)
                    response.drinks?.forEach { apiCocktail ->
                        allEntities.add(
                            CocktailEntity(
                                id = apiCocktail.id ?: "",
                                name = apiCocktail.name ?: "Unknown",
                                category = apiCocktail.category ?: "Unknown",
                                instructions = apiCocktail.instructions ?: "",
                                imageUrl = apiCocktail.imageUrl,
                                ingredients = apiCocktail.toIngredientList(),
                                isFavorite = false,
                                createdAt = Date(),
                                deletedAt = null
                            )
                        )
                    }
                }
                
                Log.d("CocktailRepo", "Inserting ${allEntities.size} cocktails into local database.")
                dao.insertAll(allEntities)
            } else {
                Log.d("CocktailRepo", "Database already has $count cocktails. Skipping API sync.")
            }
        } catch (e: Exception) {
            Log.e("CocktailRepo", "Failed to sync from API", e)
        }
    }
    
    suspend fun toggleFavorite(id: String, isFavorite: Boolean) {
        val cocktail = dao.getCocktailById(id)
        if (cocktail != null) {
            dao.update(cocktail.copy(isFavorite = isFavorite))
        }
    }

    suspend fun insertDrinkLog(log: com.supdevinci.caisson.data.local.entities.DrinkLogEntity) {
        dao.insertDrinkLog(log)
    }

    fun getDrinkLogsSince(since: Long) = dao.getDrinkLogsSince(since)

    suspend fun clearAllHistory() {
        dao.clearAllDrinkLogs()
        dao.clearAllCocktails()
    }
}
