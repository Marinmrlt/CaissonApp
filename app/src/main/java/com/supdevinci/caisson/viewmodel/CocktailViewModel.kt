package com.supdevinci.caisson.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.caisson.data.local.CocktailDatabase
import com.supdevinci.caisson.data.local.entities.CocktailEntity
import com.supdevinci.caisson.data.repository.CocktailRepository
import com.supdevinci.caisson.data.repository.UserProfile
import com.supdevinci.caisson.data.repository.UserPreferencesRepository
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
            initialValue = UserProfile("John Doe", "Male", 28, 75.0f)
        )

    init {
        viewModelScope.launch {
            cocktailRepository.syncFromApiIfEmpty()
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

    fun updateUserProfile(name: String, gender: String, age: Int, weightKg: Float) {
        viewModelScope.launch {
            userPrefsRepository.updateProfile(name, gender, age, weightKg)
        }
    }

    fun getCocktailById(id: String): StateFlow<CocktailEntity?> {
        return dao.getCocktailByIdFlow(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }
}
