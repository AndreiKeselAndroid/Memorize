package com.gmail.remember.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.MainUserCase
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
import com.gmail.remember.data.api.models.Result
import com.gmail.remember.data.api.models.asResult
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUserCase: MainUserCase
) : ViewModel() {

    private val _showDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _themes: MutableStateFlow<List<ThemeModel>?> = MutableStateFlow(null)
    val themes: StateFlow<List<ThemeModel>?> = _themes.asStateFlow()

    val stateUi: StateFlow<Result<List<ThemeModel>>> by lazy {
        mainUserCase.themes.combine(mainUserCase.settingsProfile) { themes, profile ->
            themes.map { model ->
                model.copy(
                    isChecked = model.name == profile.theme
                )
            }
        }.asResult().map { result ->
            when (result) {
                is Result.Loading -> Result.Loading
                is Result.Success -> Result.Success(result.data)
                is Result.Error -> Result.Error(result.throwable)
            }
        }
            .stateIn(viewModelScope, SharingStarted.Lazily, Result.Loading)
    }

    val photoUrl: StateFlow<String> by lazy {
        mainUserCase.settingsProfile.map { profile ->
            profile.photoUrl ?: ""
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, "")
    }

    fun addSection(name: String, themes: List<ThemeModel>) {
        viewModelScope.launch {
            if (themes.isEmpty()) mainUserCase.checkTheme(name = name)
            mainUserCase.addSection(name = name)
        }
    }

    fun showDialog() {
        viewModelScope.launch {
            _showDialog.emit(true)
        }
    }

    fun dismissDialog() {
        viewModelScope.launch {
            _showDialog.emit(false)
            _name.emit("")
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            _name.emit(name)
        }
    }

    fun deleteSection(name: String) {
        viewModelScope.launch {
            mainUserCase.deleteSection(name = name)
        }
    }

    fun setThemes(themes: List<ThemeModel>) {
        viewModelScope.launch {
            _themes.emit(themes)
        }
    }
}