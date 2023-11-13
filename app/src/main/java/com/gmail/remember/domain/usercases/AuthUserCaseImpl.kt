package com.gmail.remember.domain.usercases

import androidx.core.app.ComponentActivity
import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.ProfileModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AuthUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : AuthUserCase {

    override val profile: Flow<ProfileModel>
        get() = rememberRepository.profile

    override val firebaseAuth: FirebaseAuth
        get() = rememberRepository.firebaseAuth

    override fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit) {
        rememberRepository.signIn(
            activity = activity,
            launch = launch
        )
    }

    override fun auth(token: String, task: (Task<AuthResult>) -> Unit) {
        rememberRepository.auth(
            token = token,
            task = task
        )
    }

    override suspend fun saveProfile(profileModel: ProfileModel) {
        rememberRepository.saveProfile(profileModel = profileModel)
    }
}