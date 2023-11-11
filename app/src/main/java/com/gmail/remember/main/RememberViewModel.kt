package com.gmail.remember.main

import androidx.core.app.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.data.RememberRepository
import com.gmail.remember.domain.usercases.AuthUserCase
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RememberViewModel @Inject constructor(
    private val authUserCase: AuthUserCase
) : ViewModel() {
    val firebaseAuth: FirebaseAuth
        get() = authUserCase.firebaseAuth

    val token: StateFlow<String?> by lazy {
        authUserCase.token
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }
}