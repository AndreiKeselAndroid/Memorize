package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.ProfileSettingsModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ProfileUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : ProfileUserCase {
    override val settingsProfile: Flow<ProfileSettingsModel>
        get() = rememberRepository.settingsProfile

    override val profile: Flow<ProfileModel>
        get() = rememberRepository.profile

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
}