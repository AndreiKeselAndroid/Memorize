package com.gmail.remember.screens.addwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.AddWordsUserCase
import com.gmail.remember.models.RememberWordModel
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddWordsViewModel @Inject constructor(
    private val addWordsUserCase: AddWordsUserCase
) : ViewModel() {

    private val _enWord: MutableStateFlow<String> = MutableStateFlow("")
    val enWord: StateFlow<String> = _enWord.asStateFlow()

    private val _ruWord: MutableStateFlow<String> = MutableStateFlow("")
    val ruWord: StateFlow<String> = _ruWord.asStateFlow()

    val enableButton: StateFlow<Boolean> by lazy {
        enWord.combine(ruWord) { en, ru ->
            en.isNotEmpty() && ru.isNotEmpty()
        }
            .stateIn(viewModelScope, SharingStarted.Lazily, false)
    }

    fun setEnWord(value: String) {
        viewModelScope.launch {
            _enWord.emit(value)
        }
    }

    fun setRuWord(value: String) {
        viewModelScope.launch {
            _ruWord.emit(value)
        }
    }

    fun clickAdd(
        enWord: String,
        ruWord: String,
        result:(Task<Void>)->Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            addWordsUserCase.addWord(
                model = RememberWordModel(
                    wordEng = enWord,
                    wordRu = ruWord,
                    url = try {
                        addWordsUserCase.getWordFromDictionary(word = enWord)
                            ?.last()?.phonetics?.last()?.audio ?: ""
                    } catch (e: Exception) {
                        ""
                    }
                )
            ).addOnCompleteListener {task->
                viewModelScope.launch {
                    _ruWord.emit("")
                    _enWord.emit("")
                }
                result(task)
            }
        }
    }
}