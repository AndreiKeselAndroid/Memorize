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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUserCase: MainUserCase
) : ViewModel() {

    private val _showDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    val themes: StateFlow<List<ThemeModel>> by lazy {
        mainUserCase.themes
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    val photoUrl: StateFlow<String> by lazy {
        mainUserCase.profile.map { profile ->
            profile.photoUrl ?: ""
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, "")
    }

    fun addSection(name: String) {
        viewModelScope.launch {
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
}