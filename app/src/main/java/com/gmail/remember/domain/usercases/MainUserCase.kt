package com.gmail.remember.domain.usercases

import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.ThemeModel
import kotlinx.coroutines.flow.Flow

interface MainUserCase {

    val themes: Flow<List<ThemeModel>>

    val profile: Flow<ProfileModel>
    suspend fun addSection(name: String)
    suspend fun deleteSection(name: String)
}