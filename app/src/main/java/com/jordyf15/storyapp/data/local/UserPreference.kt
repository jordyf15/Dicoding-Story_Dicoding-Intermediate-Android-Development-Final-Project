package com.jordyf15.storyapp.data.local

import android.content.Context
import android.util.Log

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(token: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.putBoolean(LOGIN, true)
        editor.apply()
    }

    fun clearSession() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun getToken() = preferences.getString(TOKEN, "")

    fun isLogin() = preferences.getBoolean(LOGIN, false)

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
        private const val LOGIN = "isLogin"

        @Volatile
        private var instance: UserPreference? = null
        fun getInstance(
            context: Context
        ): UserPreference =
            instance ?: synchronized(this) {
                instance ?: UserPreference(context)
            }.also { instance = it }
    }
}