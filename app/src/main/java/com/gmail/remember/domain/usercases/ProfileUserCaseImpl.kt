package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.ProfileSettingsModel
import com.gmail.remember.models.ThemeModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ProfileUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : ProfileUserCase {
    override val settingsProfile: Flow<ProfileSettingsModel>
        get() = rememberRepository.settingsProfile

    override val period: Flow<Int>
        get() = rememberRepository.period

    override val themes: Flow<List<ThemeModel>>
        get() = rememberRepository.themes

    override fun words(childName: String): Flow<DataSnapshot> =
        rememberRepository.words(childName = childName)

    override suspend fun onCheckedChangeAllDays(value: Boolean) {
        rememberRepository.onCheckedChangeAllDays(value = value)
    }

    override suspend fun onCheckedChangeRemember(value: Boolean) {
        rememberRepository.onCheckedChangeRemember(value = value)
    }

    override suspend fun checkDay(name: String) {
        rememberRepository.checkDay(name = name)
    }

    override suspend fun unCheckDay(name: String) {
        rememberRepository.unCheckDay(name = name)
    }

    override suspend fun unCheckAllDays() {
        rememberRepository.unCheckAllDays()
    }

    override suspend fun checkAllDays() {
        rememberRepository.checkAllDays()
    }

    override suspend fun checkTheme(name: String) {
        rememberRepository.checkTheme(name = name)
    }

    override suspend fun setTimeFrom(time: String) {
        rememberRepository.setTimeFrom(time = time)
    }

    override suspend fun setTimeTo(time: String) {
        rememberRepository.setTimeTo(time = time)
    }

    override suspend fun setCount(count: String) {
        rememberRepository.setCount(count = count)
    }

    override suspend fun changeCountInWords(count: String) {
        rememberRepository.changeCountInWords(count = count)
    }
}