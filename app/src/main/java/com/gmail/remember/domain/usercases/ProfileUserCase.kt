package com.gmail.remember.domain.usercases

import com.gmail.remember.models.ProfileSettingsModel
import com.gmail.remember.models.ThemeModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface ProfileUserCase {

    val themes: Flow<List<ThemeModel>>

    val settingsProfile: Flow<ProfileSettingsModel>

    val period: Flow<Int>
    fun words(childName: String): Flow<DataSnapshot>
    suspend fun onCheckedChangeAllDays(value: Boolean)
    suspend fun onCheckedChangeRemember(value: Boolean)
    suspend fun checkDay(name: String)
    suspend fun unCheckDay(name: String)
    suspend fun unCheckAllDays()
    suspend fun checkAllDays()
    suspend fun checkTheme(name: String)
    suspend fun setTimeFrom(time: String)
    suspend fun setTimeTo(time: String)
    suspend fun setCount(count: String)
    suspend fun changeCountInWords(count: String)
}