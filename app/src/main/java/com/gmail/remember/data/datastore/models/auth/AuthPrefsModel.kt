package com.gmail.remember.data.datastore.models.auth

import kotlinx.coroutines.flow.Flow

interface AuthPrefsModel {

    val token: Flow<String>
    suspend fun saveToken(token: String)
}