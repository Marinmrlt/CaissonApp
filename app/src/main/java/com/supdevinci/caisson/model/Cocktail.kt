package com.supdevinci.caisson.model

import com.google.gson.annotations.SerializedName

data class CocktailResponse(
    @SerializedName("drinks")
    val drinks: List<Cocktail>?
)

data class Cocktail(
    @SerializedName("idDrink") val id: String?,
    @SerializedName("strDrink") val name: String?,
    @SerializedName("strDrinkThumb") val imageUrl: String?,
    @SerializedName("strInstructions") val instructions: String?,
    
    // The API lazily returns 15 distinct column names instead of an array...
    @SerializedName("strIngredient1") val strIngredient1: String?,
    @SerializedName("strIngredient2") val strIngredient2: String?,
    @SerializedName("strIngredient3") val strIngredient3: String?,
    @SerializedName("strIngredient4") val strIngredient4: String?,
    @SerializedName("strIngredient5") val strIngredient5: String?,
    @SerializedName("strIngredient6") val strIngredient6: String?,
    @SerializedName("strIngredient7") val strIngredient7: String?,
    @SerializedName("strIngredient8") val strIngredient8: String?,
    @SerializedName("strIngredient9") val strIngredient9: String?,
    @SerializedName("strIngredient10") val strIngredient10: String?,
    @SerializedName("strIngredient11") val strIngredient11: String?,
    @SerializedName("strIngredient12") val strIngredient12: String?,
    @SerializedName("strIngredient13") val strIngredient13: String?,
    @SerializedName("strIngredient14") val strIngredient14: String?,
    @SerializedName("strIngredient15") val strIngredient15: String?,

    @SerializedName("strMeasure1") val strMeasure1: String?,
    @SerializedName("strMeasure2") val strMeasure2: String?,
    @SerializedName("strMeasure3") val strMeasure3: String?,
    @SerializedName("strMeasure4") val strMeasure4: String?,
    @SerializedName("strMeasure5") val strMeasure5: String?,
    @SerializedName("strMeasure6") val strMeasure6: String?,
    @SerializedName("strMeasure7") val strMeasure7: String?,
    @SerializedName("strMeasure8") val strMeasure8: String?,
    @SerializedName("strMeasure9") val strMeasure9: String?,
    @SerializedName("strMeasure10") val strMeasure10: String?,
    @SerializedName("strMeasure11") val strMeasure11: String?,
    @SerializedName("strMeasure12") val strMeasure12: String?,
    @SerializedName("strMeasure13") val strMeasure13: String?,
    @SerializedName("strMeasure14") val strMeasure14: String?,
    @SerializedName("strMeasure15") val strMeasure15: String?
) {
    fun toIngredientList(): List<IngredientMeasure> {
        val list = mutableListOf<IngredientMeasure>()
        if (!strIngredient1.isNullOrBlank()) list.add(IngredientMeasure(strIngredient1, strMeasure1))
        if (!strIngredient2.isNullOrBlank()) list.add(IngredientMeasure(strIngredient2, strMeasure2))
        if (!strIngredient3.isNullOrBlank()) list.add(IngredientMeasure(strIngredient3, strMeasure3))
        if (!strIngredient4.isNullOrBlank()) list.add(IngredientMeasure(strIngredient4, strMeasure4))
        if (!strIngredient5.isNullOrBlank()) list.add(IngredientMeasure(strIngredient5, strMeasure5))
        if (!strIngredient6.isNullOrBlank()) list.add(IngredientMeasure(strIngredient6, strMeasure6))
        if (!strIngredient7.isNullOrBlank()) list.add(IngredientMeasure(strIngredient7, strMeasure7))
        if (!strIngredient8.isNullOrBlank()) list.add(IngredientMeasure(strIngredient8, strMeasure8))
        if (!strIngredient9.isNullOrBlank()) list.add(IngredientMeasure(strIngredient9, strMeasure9))
        if (!strIngredient10.isNullOrBlank()) list.add(IngredientMeasure(strIngredient10, strMeasure10))
        if (!strIngredient11.isNullOrBlank()) list.add(IngredientMeasure(strIngredient11, strMeasure11))
        if (!strIngredient12.isNullOrBlank()) list.add(IngredientMeasure(strIngredient12, strMeasure12))
        if (!strIngredient13.isNullOrBlank()) list.add(IngredientMeasure(strIngredient13, strMeasure13))
        if (!strIngredient14.isNullOrBlank()) list.add(IngredientMeasure(strIngredient14, strMeasure14))
        if (!strIngredient15.isNullOrBlank()) list.add(IngredientMeasure(strIngredient15, strMeasure15))
        return list
    }
}
