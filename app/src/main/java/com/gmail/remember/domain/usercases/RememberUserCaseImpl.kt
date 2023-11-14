package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.RememberWordModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RememberUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : RememberUserCase {

    override val words: Flow<DataSnapshot>
        get() = rememberRepository.words
    override val profile: Flow<ProfileModel>
        get() = rememberRepository.profile

    override suspend fun deleteWords(models: List<RememberWordModel?>) {
        rememberRepository.deleteWords(models = models)
    }
}