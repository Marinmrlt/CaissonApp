package com.supdevinci.caisson.service

import com.supdevinci.caisson.model.CocktailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    suspend fun getRandomCocktail(): CocktailResponse

    @GET("search.php")
    suspend fun searchCocktails(@Query("s") query: String): CocktailResponse

    @GET("lookup.php")
    suspend fun getCocktailById(@Query("i") id: String): CocktailResponse
}
