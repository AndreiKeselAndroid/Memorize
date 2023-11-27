package com.gmail.remember.domain.usercases

import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.ProfileSettingsModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface ProfileUserCase {

    val settingsProfile: Flow<ProfileSettingsModel>

    val profile: Flow<ProfileModel>
    fun words(childName: String): Flow<DataSnapshot>
    suspend fun onCheckedChangeAllDays(value: Boolean)
    suspend fun onCheckedChangeRemember(value: Boolean)
    suspend fun checkDay(name: String)
    suspend fun unCheckDay(name: String)
    suspend fun unCheckAllDays()
    suspend fun checkAllDays()
}