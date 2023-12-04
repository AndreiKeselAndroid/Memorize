package com.gmail.remember.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.models.DayModel
import com.gmail.remember.domain.usercases.ProfileUserCase
import com.gmail.remember.models.ThemeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DEFAULT_COUNT_THEMES = 1

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val profileUserCase: ProfileUserCase,
) : ViewModel() {

    private val _countThemes: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_COUNT_THEMES)
    val countThemes: StateFlow<Int> = _countThemes.asStateFlow()

    private val _isShowAllThemes: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isShowAllThemes: StateFlow<Boolean> = _isShowAllThemes.asStateFlow()

    val checkedAllDays: StateFlow<Boolean> by lazy {
        profileUserCase.settingsProfile.map { model ->
            model.allDays.toString().toBoolean()
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, true)
    }

    val themes: StateFlow<List<ThemeModel>> =
        profileUserCase.themes.combine(profileUserCase.settingsProfile) { themes, profile ->
            themes.map { model ->
                model.copy(
                    isChecked = model.name == profile.theme
                )
            }
        }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


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


    fun checkTheme(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.checkTheme(name = name)
        }
    }

    fun setCountThemes(count: Int, isShowAllThemes: Boolean) {
        viewModelScope.launch {
            _isShowAllThemes.emit(isShowAllThemes.not())
            _countThemes.emit(count)
        }
    }
}