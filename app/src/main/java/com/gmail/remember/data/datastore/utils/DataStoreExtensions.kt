package com.gmail.remember.data.datastore.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.gmail.remember.data.datastore.serializers.ProfilePreferencesSerializer
import com.gmail.remember.models.ProfileModel

internal const val PROFILE_PREFS_FILE_NAME = "profile_prefs.pb"


internal val Context.profileDataStore: DataStore<ProfileModel> by dataStore(
    fileName = PROFILE_PREFS_FILE_NAME,
    serializer = ProfilePreferencesSerializer()
)