package com.gmail.remember.domain.usercases

import com.gmail.remember.models.ProfileSettingsModel
import com.gmail.remember.models.WordModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface WordsUserCase {

    val settingsProfile: Flow<ProfileSettingsModel>
    suspend fun deleteWords(models: List<WordModel?>, childName: String)
    fun words(childName: String): Flow<DataSnapshot>
}