package com.gmail.remember.domain.usercases

import com.gmail.remember.models.ProfileSettingsModel
import com.gmail.remember.models.ThemeModel
import kotlinx.coroutines.flow.Flow

interface MainUserCase {

    val themes: Flow<List<ThemeModel>>

    val settingsProfile: Flow<ProfileSettingsModel>
    suspend fun addSection(name: String)
    suspend fun deleteSection(name: String)
    suspend fun checkTheme(name: String)
}