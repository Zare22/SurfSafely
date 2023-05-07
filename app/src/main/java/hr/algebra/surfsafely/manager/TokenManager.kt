package hr.algebra.surfsafely.manager

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "token_prefs")

object TokenManager {
    private val TOKEN_KEY = stringPreferencesKey("token_key")

    suspend fun getToken(context: Context): String? {
        val dataStore = context.dataStore
        val token = dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
        return token.firstOrNull()
    }

    suspend fun setToken(context: Context, token: String) {
        val dataStore = context.dataStore
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearToken(context: Context) {
        val dataStore = context.dataStore
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    suspend fun hasToken(context: Context): Boolean {
        val dataStore = context.dataStore
        val token = dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
        return token.firstOrNull() != null
    }
}