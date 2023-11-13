package com.gmail.remember.data.datastore.models.auth

import androidx.datastore.core.DataStore
import com.gmail.remember.models.ProfileModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ProfilePrefsModelImpl @Inject constructor(
    private val dataStore: DataStore<ProfileModel>
) : ProfilePrefsModel {
    override val profileModel: Flow<ProfileModel>
        get() = dataStore.data

    override suspend fun saveProfile(profileModel: ProfileModel) {
        dataStore.updateData { model ->
            model.copy(
                idToken = profileModel.idToken,
                displayName = profileModel.displayName,
                familyName = profileModel.familyName,
                givenName = profileModel.givenName,
                photoUrl = profileModel.photoUrl
            )
        }
    }
}