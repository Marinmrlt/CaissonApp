package com.supdevinci.caisson.data.local.dao

import androidx.room.*
import com.supdevinci.caisson.data.local.entities.CocktailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cocktails: List<CocktailEntity>)

    @Update
    suspend fun update(cocktail: CocktailEntity)

    @Delete
    suspend fun delete(cocktail: CocktailEntity)

    // Using deletedAt IS NULL for soft delete feature built earlier
    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL ORDER BY name ASC")
    fun getAllCocktails(): Flow<List<CocktailEntity>>

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL")
    suspend fun getAllCocktailsSync(): List<CocktailEntity>

    @Query("SELECT * FROM cocktails WHERE id = :id LIMIT 1")
    suspend fun getCocktailById(id: String): CocktailEntity?
    
    @Query("SELECT * FROM cocktails WHERE id = :id LIMIT 1")
    fun getCocktailByIdFlow(id: String): Flow<CocktailEntity?>

    // --- Drink Logs Analytics ---
    @Insert
    suspend fun insertDrinkLog(log: com.supdevinci.caisson.data.local.entities.DrinkLogEntity)

    @Query("SELECT * FROM drink_logs WHERE timestamp >= :since ORDER BY timestamp DESC")
    fun getDrinkLogsSince(since: Long): Flow<List<com.supdevinci.caisson.data.local.entities.DrinkLogEntity>>

    @Query("DELETE FROM cocktails")
    suspend fun clearAllCocktails()

    @Query("DELETE FROM drink_logs")
    suspend fun clearAllDrinkLogs()
}
