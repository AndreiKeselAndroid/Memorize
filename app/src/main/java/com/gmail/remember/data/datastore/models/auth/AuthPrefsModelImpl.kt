package com.gmail.remember.data.datastore.models.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val AUTH_KEY = stringPreferencesKey("AUTH_KEY")

internal class AuthPrefsModelImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AuthPrefsModel {
    override val token: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[AUTH_KEY] ?: ""
        }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_KEY] = token
        }
    }
}