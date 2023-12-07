package com.gmail.remember.screens.words

import android.media.MediaPlayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.WordsUserCase
import com.gmail.remember.models.WordModel
import com.gmail.remember.navigation.CHILD_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WordsViewModel @Inject constructor(
    private val rememberUserCase: WordsUserCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val mediaPlayer: MediaPlayer = MediaPlayer()

    val countSuccess: StateFlow<Int> by lazy {
        rememberUserCase.settingsProfile.map { model ->
            model.countSuccess.toInt()
        }
            .stateIn(viewModelScope, SharingStarted.Lazily, 5)
    }

    val childName: StateFlow<String> by lazy {
        savedStateHandle.getStateFlow(CHILD_NAME, "")
    }

    private val _selectedWords: MutableStateFlow<List<WordModel?>> =
        MutableStateFlow(emptyList())
    val selectedWords: StateFlow<List<WordModel?>> = _selectedWords.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val words: StateFlow<List<WordModel?>?> by lazy {
        childName.flatMapLatest { name ->
            delay(1000)
            rememberUserCase.words(name).combine(_selectedWords.asStateFlow()) { snapshots, words ->
                snapshots.children.map { snapshot ->
                    if (words.contains(snapshot.getValue(WordModel::class.java)))
                        snapshot.getValue(WordModel::class.java)?.copy(isCheck = true)
                    else snapshot.getValue(WordModel::class.java)
                }
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    fun enableMultiSelect(word: WordModel, words: List<WordModel?>, childName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (words.size > 1) {
                _selectedWords.update { words ->
                    words.toMutableList().apply {
                        add(word)
                    }.toList()
                }
            } else deleteWords(listOf(word), childName = childName)
        }
    }

    fun disableMultiSelect() {
        viewModelScope.launch(Dispatchers.IO) {
            unSelectAllWords()
        }
    }

    fun selectWord(word: WordModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedWords.update { words ->
                words.toMutableList().apply {
                    if (word.isCheck) {
                        clear()
                        addAll(words.filter { model -> model?.wordEng != word.wordEng })
                    } else add(word)
                }.toList()
            }
        }
    }

    fun selectedAllHandler(
        allWords: List<WordModel?>,
        selectedWords: List<WordModel?>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (allWords.size == selectedWords.size) unSelectAllWords()
            else selectAllWords(allWords = allWords)
        }
    }

    private fun selectAllWords(allWords: List<WordModel?>) {
        _selectedWords.update { words ->
            words.toMutableList().apply {
                addAll(allWords.filter { model -> model?.isCheck == false })
            }.toList()
        }
    }

    private fun unSelectAllWords() {
        _selectedWords.update { words ->
            words.toMutableList().apply {
                clear()
            }.toList()
        }
    }

    fun deleteWords(models: List<WordModel?>, childName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            rememberUserCase.deleteWords(models = models, childName = childName)
            unSelectAllWords()
        }
    }
}