package com.gmail.remember.screens.settings

import androidx.lifecycle.ViewModel
import com.gmail.remember.domain.usercases.AuthUserCase
import com.gmail.remember.domain.usercases.SettingsUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val settingsUserCase: SettingsUserCase,
    private val authUserCase: AuthUserCase
) : ViewModel() {
    fun signOut(exit: () -> Unit) {
        authUserCase.firebaseAuth.signOut()
        exit()
    }
}