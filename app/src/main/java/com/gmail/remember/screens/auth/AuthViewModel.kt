package com.gmail.remember.screens.auth

import androidx.core.app.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.AuthUserCase
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUserCase: AuthUserCase
) : ViewModel() {

    private val _isShowProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isShowProgress: StateFlow<Boolean> = _isShowProgress.asStateFlow()

    fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit) {
        viewModelScope.launch {
            _isShowProgress.emit(true)
            authUserCase.signIn(
                activity = activity,
                launch = launch
            )
        }
    }

    fun auth(token: String, task: (Task<AuthResult>) -> Unit) {
        authUserCase.auth(
            token = token,
            task = task
        )
    }

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authUserCase.saveToken(token = token)
        }
    }
}