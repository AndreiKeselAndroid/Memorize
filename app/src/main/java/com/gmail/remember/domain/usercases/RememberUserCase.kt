package com.gmail.remember.domain.usercases

import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.RememberWordModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface RememberUserCase {

    val words: Flow<DataSnapshot>

    val profile: Flow<ProfileModel>
    suspend fun deleteWords(models: List<RememberWordModel?>)
}