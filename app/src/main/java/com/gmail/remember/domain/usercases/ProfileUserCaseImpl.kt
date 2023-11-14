package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.ProfileModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ProfileUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : ProfileUserCase {
    override val profile: Flow<ProfileModel>
        get() = rememberRepository.profile
}