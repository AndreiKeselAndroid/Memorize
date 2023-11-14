package com.gmail.remember.domain.usercases

import com.gmail.remember.models.ProfileModel
import kotlinx.coroutines.flow.Flow

interface ProfileUserCase {
    val profile: Flow<ProfileModel>
}