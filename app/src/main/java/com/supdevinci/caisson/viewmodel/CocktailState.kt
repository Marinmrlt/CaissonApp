package com.supdevinci.caisson.viewmodel

import com.supdevinci.caisson.data.local.entities.CocktailEntity

sealed interface CocktailState {
    data object Loading : CocktailState
    data object Empty : CocktailState
    data class Success(val cocktails: List<CocktailEntity>) : CocktailState
    data class Error(val message: String) : CocktailState
}
