package com.gmail.remember.screens.addwords

import androidx.lifecycle.ViewModel
import com.gmail.remember.domain.usercases.AddWordsUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AddWordsViewModel @Inject constructor(
    private val addWordsUserCase: AddWordsUserCase
) : ViewModel() {
}