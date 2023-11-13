package com.gmail.remember.screens.auth

import androidx.core.app.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.AuthUserCase
import com.gmail.remember.utils.toModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUserCase: AuthUserCase
) : ViewModel() {

    fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit) {
        authUserCase.signIn(
            activity = activity,
            launch = launch
        )
    }

    fun auth(token: String, task: (Task<AuthResult>) -> Unit) {
        authUserCase.auth(
            token = token,
            task = task
        )
    }

    fun saveProfile(account: GoogleSignInAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            authUserCase.saveProfile(profileModel = account.toModel())
        }
    }
}