package com.gmail.remember.screens.addwords

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.AddWordsUserCase
import com.gmail.remember.models.WordModel
import com.gmail.remember.navigation.CHILD_NAME
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

const val LICENSE = "BY 3.0 US"

@HiltViewModel
internal class AddWordsViewModel @Inject constructor(
    private val addWordsUserCase: AddWordsUserCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val childName: StateFlow<String> by lazy {
        savedStateHandle.getStateFlow(CHILD_NAME, "")
    }

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
        childName: String,
        result: (Task<Void>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            addWordsUserCase.addWord(
                model = WordModel(
                    wordEng = enWord,
                    wordRu = ruWord,
                    url = try {
                        var url = ""
                        addWordsUserCase.getWordFromDictionary(word = enWord)?.forEach { item ->
                            item.phonetics.forEach { phonetic ->
                                if (phonetic?.license?.name == LICENSE)
                                    url = phonetic.audio
                                else phonetic?.audio
                            }
                        }
                        url
                    } catch (e: Exception) {
                        ""
                    },
                ),
                childName = childName
            ).addOnCompleteListener { task ->
                viewModelScope.launch {
                    _ruWord.emit("")
                    _enWord.emit("")
                }
                result(task)
            }
        }
    }
}