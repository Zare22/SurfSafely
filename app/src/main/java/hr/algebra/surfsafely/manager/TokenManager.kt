package hr.algebra.surfsafely.manager

import android.content.Context

object TokenManager {
    private const val TOKEN_PREFS = "token_prefs"
    private const val TOKEN_KEY = "token_key"
    private var token: String? = null

    fun getToken(context: Context): String? {
        if (token == null) {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS, Context.MODE_PRIVATE)
            token = sharedPreferences.getString(TOKEN_KEY, null)
        }
        return token
    }

    fun setToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
        this.token = token
    }

    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
        token = null
    }

    fun hasToken(context: Context): Boolean {
        return getToken(context) != null
    }
}