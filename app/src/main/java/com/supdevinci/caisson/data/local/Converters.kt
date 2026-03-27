package com.supdevinci.caisson.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.supdevinci.caisson.model.IngredientMeasure
import java.util.Date

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromIngredientList(value: List<IngredientMeasure>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIngredientList(value: String?): List<IngredientMeasure> {
        val listType = object : TypeToken<List<IngredientMeasure>>() {}.type
        return if (value.isNullOrEmpty()) emptyList() else gson.fromJson(value, listType)
    }
}
