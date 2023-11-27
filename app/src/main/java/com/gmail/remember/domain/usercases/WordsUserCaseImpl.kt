package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.ProfileSettingsModel
import com.gmail.remember.models.WordModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class WordsUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : WordsUserCase {
    override val settingsProfile: Flow<ProfileSettingsModel>
        get() = rememberRepository.settingsProfile

    override suspend fun deleteWords(models: List<WordModel?>, childName: String) {
        rememberRepository.deleteWords(models = models, childName = childName)
    }

    override fun words(childName: String): Flow<DataSnapshot> =
        rememberRepository.words(childName = childName)
}