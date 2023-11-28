package com.gmail.remember.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.models.DayModel
import com.gmail.remember.domain.usercases.ProfileUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val profileUserCase: ProfileUserCase,
) : ViewModel() {

    val checkedAllDays: StateFlow<Boolean> by lazy {
        profileUserCase.settingsProfile.map { model ->
            model.allDays.toString().toBoolean()
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, true)
    }

    val isRemember: StateFlow<Boolean> by lazy {
        profileUserCase.settingsProfile.map { model ->
            model.remember.toString().toBoolean()
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, false)
    }

    val days: StateFlow<List<DayModel>> by lazy {
        profileUserCase.settingsProfile.map { profile ->
            profile.days.values.sortedBy { model ->
                model.id
            }.toList()
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }


    val displayName: StateFlow<String> by lazy {
        profileUserCase.profile.map { profile ->
            profile.displayName ?: ""
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, "")
    }

    fun onCheckedChangeRemember(value: Boolean, unit: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.onCheckedChangeRemember(value = value)
            unit()
        }
    }

    fun onCheckedChangeAllDays(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.onCheckedChangeAllDays(value = value)
            if (value) profileUserCase.checkAllDays()
            else profileUserCase.unCheckAllDays()
        }
    }

    fun checkDay(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.checkDay(name = name)
        }
    }

    fun unCheckDay(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.unCheckDay(name = name)
        }
    }
}