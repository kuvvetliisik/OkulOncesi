package com.example.okuloncesi.utils

import android.content.Context

object ProgressManager {
    private const val PREF_NAME = "user_progress"

    fun saveProgress(context: Context, key: String, value: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getProgress(context: Context, key: String): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, false)
    }
}
