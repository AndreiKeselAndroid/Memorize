package com.gmail.remember.data.datastore.models.auth

import com.gmail.remember.models.ProfileModel
import kotlinx.coroutines.flow.Flow

interface ProfilePrefsModel {

    val profileModel: Flow<ProfileModel>
    suspend fun saveProfile(profileModel: ProfileModel)
}