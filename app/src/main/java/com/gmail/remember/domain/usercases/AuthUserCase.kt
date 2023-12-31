package com.gmail.remember.domain.usercases

import androidx.core.app.ComponentActivity
import com.gmail.remember.models.ProfileSettingsModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

interface AuthUserCase {

    val settingsProfile: Flow<ProfileSettingsModel>

    val firebaseAuth: FirebaseAuth

    suspend fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit)

    suspend fun auth(token: String, task: (Task<AuthResult>) -> Unit)

    suspend fun saveProfile(profileModel: ProfileSettingsModel)
}