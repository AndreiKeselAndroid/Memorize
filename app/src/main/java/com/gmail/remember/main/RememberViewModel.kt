package com.gmail.remember.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.AuthUserCase
import com.gmail.remember.models.ProfileSettingsModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class RememberViewModel @Inject constructor(
    private val authUserCase: AuthUserCase
) : ViewModel() {
    val firebaseAuth: FirebaseAuth
        get() = authUserCase.firebaseAuth

    val settingsProfile: StateFlow<ProfileSettingsModel?> by lazy {
        authUserCase.settingsProfile
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }
}