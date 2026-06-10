package com.example.moneymap.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val KEY_USER_ID = longPreferencesKey("user_id")
        val KEY_CURRENCY = stringPreferencesKey("currency")
    }

    val userId: Flow<Long> = dataStore.data.map { prefs ->
        prefs[KEY_USER_ID] ?: -1L
    }
    val currency: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_CURRENCY] ?: "IRR"
    }

    suspend fun saveUserId(id: Long) {
        dataStore.edit { prefs -> prefs[KEY_USER_ID] = id }
    }

    suspend fun saveCurrency(currency: String) {
        dataStore.edit { prefs -> prefs[KEY_CURRENCY] = currency }
    }

    // موقع logout تمام داده‌ها پاک می‌شوند
    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}