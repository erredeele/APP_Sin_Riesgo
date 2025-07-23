package com.example.Sin_Riesgo.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.Sin_Riesgo.domain.models.AuthInfo

private val Context.dataStore by preferencesDataStore(name = "auth_preferences")

class AuthLocalDataSource(private val context: Context) {

    private object PreferencesKeys {
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_PROFILE_IMAGE_URL = stringPreferencesKey("user_profile_image_url") // <-- ¡NUEVA CLAVE!
        val IS_LOGGED_IN = stringPreferencesKey("is_logged_in")
    }

    val authInfo: Flow<AuthInfo?> = context.dataStore.data
        .map { preferences ->
            val userId = preferences[PreferencesKeys.USER_ID]
            val email = preferences[PreferencesKeys.USER_EMAIL]
            val name = preferences[PreferencesKeys.USER_NAME]
            val profileImageUrl = preferences[PreferencesKeys.USER_PROFILE_IMAGE_URL] // <-- Leer nueva clave
            if (userId != null) {
                AuthInfo(userId, email, name, profileImageUrl) // <-- Pasar nueva clave
            } else {
                null
            }
        }

    val userEmail: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_EMAIL]
        }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] == "true"
        }

    // Modificado: Ahora acepta profileImageUrl
    suspend fun saveUserSession(email: String?, name: String?, userId: String, profileImageUrl: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_EMAIL] = email ?: ""
            preferences[PreferencesKeys.USER_NAME] = name ?: ""
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.USER_PROFILE_IMAGE_URL] = profileImageUrl ?: "" // <-- Guardar nueva clave
            preferences[PreferencesKeys.IS_LOGGED_IN] = "true"
        }
    }

    suspend fun clearUserSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}