package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.ThemeModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class MainUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : MainUserCase {
    override val themes: Flow<List<ThemeModel>>
        get() = rememberRepository.themes

    override val profile: Flow<ProfileModel>
        get() = rememberRepository.profile

    override suspend fun addSection(name: String) {
        rememberRepository.addSection(name = name)
    }

    override suspend fun deleteSection(name: String) {
        rememberRepository.deleteSection(name = name)
    }
}