package com.gmail.remember.domain.usercases

import androidx.core.app.ComponentActivity
import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.ProfileSettingsModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AuthUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : AuthUserCase {

    override val settingsProfile: Flow<ProfileSettingsModel>
        get() = rememberRepository.settingsProfile

    override val firebaseAuth: FirebaseAuth
        get() = rememberRepository.firebaseAuth

    override suspend fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit) {
        rememberRepository.signIn(
            activity = activity,
            launch = launch
        )
    }

    override suspend fun auth(token: String, task: (Task<AuthResult>) -> Unit) {
        rememberRepository.auth(
            token = token,
            task = task
        )
    }

    override suspend fun saveProfile(profileModel: ProfileSettingsModel) {
        rememberRepository.saveProfile(profileModel = profileModel)
    }
}