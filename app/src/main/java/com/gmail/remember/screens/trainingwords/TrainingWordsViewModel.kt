package com.gmail.remember.screens.trainingwords

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.ProfileUserCase
import com.gmail.remember.domain.usercases.TrainingWordsUserCase
import com.gmail.remember.models.WordModel
import com.gmail.remember.navigation.WORD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
internal class TrainingWordsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val trainingWordsUserCase: TrainingWordsUserCase,
    private val profileUserCase: ProfileUserCase
) : ViewModel() {

    private val _answer: MutableStateFlow<String> = MutableStateFlow("")
    val answer: StateFlow<String> = _answer.asStateFlow()

    private val _error: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val wordModelDeepLink: StateFlow<WordModel?> by lazy {
        profileUserCase.settingsProfile.flatMapLatest { profile ->
            profileUserCase.words(profile.theme)
                .combine(savedStateHandle.getStateFlow(WORD, "")) { data, word ->
                    data.children.map { snapshot ->
                        snapshot.getValue(WordModel::class.java)
                    }.firstOrNull { model ->
                        model?.wordEng == word
                    }
                }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val wordModel: StateFlow<WordModel?> by lazy {
        profileUserCase.settingsProfile.flatMapLatest { profile ->
            profileUserCase.words(profile.theme).map { data ->
                val words: List<WordModel?> = data.children.map { snapshot ->
                    snapshot.getValue(WordModel::class.java)
                }.filter { word ->
                    word?.countSuccess != profile.countSuccess.toInt()
                }
                if (words.isNotEmpty() && words.size > 1) words[Random.nextInt(
                    0,
                    words.size
                )]
                else if (words.size == 1) words[0] else null
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    fun setAnswer(value: String) {
        viewModelScope.launch {
            _answer.emit(value)
        }
    }

    fun onCheckAnswer(word: WordModel, answer: String, errorMessage: String) {
        viewModelScope.launch {
            if (word.wordRu.isNotEmpty() && answer.isNotEmpty()) {
                if (word.wordRu.uppercase() == answer.uppercase()) {
                    savedStateHandle[WORD] = ""
                    trainingWordsUserCase.changeCountSuccessInWord(wordModel = word)
                    setAnswer("")
                    setErrorMessage(message = "")
                } else {
                    _error.emit(true)
                    setErrorMessage(message = errorMessage)
                    savedStateHandle[WORD] = ""
                    trainingWordsUserCase.changeCountErrorInWord(wordModel = word)
                    setAnswer("")
                }
            }
        }
    }

    private fun setErrorMessage(message: String) {
        viewModelScope.launch {
            _errorMessage.emit(message)
        }
    }

    fun onNext() {
        viewModelScope.launch {
            _error.emit(false)
            _errorMessage.emit("")
        }
    }
}