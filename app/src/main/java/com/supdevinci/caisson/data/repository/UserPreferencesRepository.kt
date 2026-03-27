package com.supdevinci.caisson.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

data class UserProfile(
    val name: String,
    val gender: String, // "Male" or "Female"
    val age: Int,
    val weightKg: Float,
    val isBiometricEnabled: Boolean
)

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val NAME_KEY = stringPreferencesKey("user_name")
        val GENDER_KEY = stringPreferencesKey("user_gender")
        val AGE_KEY = intPreferencesKey("user_age")
        val WEIGHT_KEY = floatPreferencesKey("user_weight")
        val BIOMETRIC_KEY = booleanPreferencesKey("user_biometric")
    }

    val userProfileFlow: Flow<UserProfile> = context.dataStore.data
        .map { preferences ->
            UserProfile(
                name = preferences[NAME_KEY] ?: "John Doe",
                gender = preferences[GENDER_KEY] ?: "Male",
                age = preferences[AGE_KEY] ?: 28,
                weightKg = preferences[WEIGHT_KEY] ?: 75.0f,
                isBiometricEnabled = preferences[BIOMETRIC_KEY] ?: false
            )
        }

    suspend fun updateProfile(name: String, gender: String, age: Int, weightKg: Float, isBiometricEnabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = name
            prefs[GENDER_KEY] = gender
            prefs[AGE_KEY] = age
            prefs[WEIGHT_KEY] = weightKg
            prefs[BIOMETRIC_KEY] = isBiometricEnabled
        }
    }
}
