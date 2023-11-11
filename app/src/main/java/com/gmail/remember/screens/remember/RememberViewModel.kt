package com.gmail.remember.screens.remember

import androidx.lifecycle.ViewModel
import com.gmail.remember.domain.usercases.RememberUserCase
import com.gmail.remember.models.RememberWordModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class RememberViewModel @Inject constructor(
    private val rememberUserCase: RememberUserCase
): ViewModel() {

    private val _words: MutableStateFlow<List<RememberWordModel>> = MutableStateFlow(emptyList())
    val words: StateFlow<List<RememberWordModel>> = _words.asStateFlow()

}