package com.supdevinci.caisson.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.caisson.data.local.CocktailDatabase
import com.supdevinci.caisson.data.local.entities.CocktailEntity
import com.supdevinci.caisson.data.repository.CocktailRepository
import com.supdevinci.caisson.data.repository.UserProfile
import com.supdevinci.caisson.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class CocktailViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = CocktailDatabase.getDatabase(application).cocktailDao()
    private val cocktailRepository = CocktailRepository(dao)
    private val userPrefsRepository = UserPreferencesRepository(application)

    // UI State for CocktailScreen
    val state: StateFlow<CocktailState> = cocktailRepository.allCocktails
        .map { cocktails ->
            if (cocktails.isEmpty()) CocktailState.Empty
            else CocktailState.Success(cocktails)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CocktailState.Loading
        )

    // Legacy property for other screens
    // Added to tether the new splash screen!
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    val allCocktails: StateFlow<List<CocktailEntity>> = cocktailRepository.allCocktails
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userProfile: StateFlow<UserProfile> = userPrefsRepository.userProfileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile("John Doe", "Male", 28, 75.0f, false)
        )

    init {
        // Trigger offline-first sync when ViewModel is created
        viewModelScope.launch {
            cocktailRepository.syncFromApiIfEmpty()
            _isReady.value = true // Sync finished, drop splash screen
        }
    }

    fun addCocktail(name: String, instructions: String) {
        viewModelScope.launch {
            val newCocktail = CocktailEntity(
                id = UUID.randomUUID().toString(),
                name = name,
                instructions = instructions
            )
            dao.insertAll(listOf(newCocktail))
        }
    }

    fun toggleFavorite(cocktail: CocktailEntity) {
        toggleFavorite(cocktail.id, !cocktail.isFavorite)
    }

    fun toggleFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            cocktailRepository.toggleFavorite(id, isFavorite)
        }
    }

    fun archiveCocktail(id: String) {
        viewModelScope.launch {
            val cocktail = dao.getCocktailById(id)
            if (cocktail != null) {
                dao.update(cocktail.copy(deletedAt = java.util.Date()))
            }
        }
    }

    fun updateUserProfile(name: String, gender: String, age: Int, weightKg: Float, isBiometricEnabled: Boolean) {
        viewModelScope.launch {
            userPrefsRepository.updateProfile(name, gender, age, weightKg, isBiometricEnabled)
        }
    }

    fun getCocktailById(id: String): StateFlow<CocktailEntity?> {
        return dao.getCocktailByIdFlow(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            cocktailRepository.clearAllHistory()
        }
    }

    // --- Tracker & Widmark Formula ---

    // Look back exactly 24 hours
    private val past24HoursMs = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
    
    val recentDrinks = cocktailRepository.getDrinkLogsSince(past24HoursMs)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val liveBac: StateFlow<Float> = kotlinx.coroutines.flow.combine(userProfile, recentDrinks) { profile, drinks ->
        if(drinks.isEmpty()) return@combine 0.00f

        // Widmark Formula calculations:
        // BAC = [Alcohol consumed in grams / (Body weight in grams x r)] x 100
        // r = Widmark factor (0.68 for men, 0.55 for women)
        
        val r = if(profile.gender.equals("Male", ignoreCase = true)) 0.68f else 0.55f
        val weightGrams = profile.weightKg * 1000f

        var totalGramsAlcohol = 0f
        val now = java.util.Date()

        for(drink in drinks) {
            // Find hours elapsed since drink was tracked
            val hoursElapsed = (now.time - drink.timestamp.time) / (1000f * 60 * 60)
            
            // Alcohol in grams = Volume (ml) x ABV x 0.789 (density of ethanol)
            val grams = drink.volumeMl * drink.abvEstimated * 0.789f
            
            // Average metabolic burn rate is roughly 0.015 BAC units per hour, 
            // but we subtract the absolute consumed grams burned over time.
            // Simplified: we'll calculate theoretical peak BAC for this drink, then subtract decay.
            val peakBAC = (grams / (weightGrams * r)) * 100f
            var activeBACForDrink = peakBAC - (0.015f * hoursElapsed)
            if(activeBACForDrink < 0) activeBACForDrink = 0f

            totalGramsAlcohol += activeBACForDrink
        }

        totalGramsAlcohol
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.00f)

    fun addDrink(cocktailId: String, name: String, quantity: Int) {
        viewModelScope.launch {
            // Log it N times 
            repeat(quantity) {
                cocktailRepository.insertDrinkLog(
                    com.supdevinci.caisson.data.local.entities.DrinkLogEntity(
                        cocktailId = cocktailId,
                        cocktailName = name,
                        abvEstimated = 0.13f, // General estimate for UI demo
                        volumeMl = 250
                    )
                )
            }
        }
    }
}
