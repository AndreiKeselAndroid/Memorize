package com.gmail.remember.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.ProfileUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val settingsUserCase: ProfileUserCase,
) : ViewModel() {

    private val _checked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val checked: StateFlow<Boolean> = _checked.asStateFlow()

    val displayName: StateFlow<String> by lazy {
        settingsUserCase.profile.map { profile ->
            profile.displayName ?: ""
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, "")
    }

    fun onCheckedChange(value: Boolean){
        viewModelScope.launch {
            _checked.emit(value)
        }
    }
}