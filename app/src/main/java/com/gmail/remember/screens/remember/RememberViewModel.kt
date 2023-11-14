package com.gmail.remember.screens.remember

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.domain.usercases.RememberUserCase
import com.gmail.remember.models.RememberWordModel
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RememberViewModel @Inject constructor(
    private val rememberUserCase: RememberUserCase
) : ViewModel() {

    private val _selectedWords: MutableStateFlow<List<RememberWordModel?>> =
        MutableStateFlow(emptyList())
    val selectedWords: StateFlow<List<RememberWordModel?>> = _selectedWords.asStateFlow()

    val photoUrl: StateFlow<String> by lazy {
        rememberUserCase.profile.map { profile ->
            profile.photoUrl ?: ""
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, "")
    }

    val words: StateFlow<List<RememberWordModel?>> by lazy {
        rememberUserCase.words.combine(_selectedWords.asStateFlow()) { snapshots, words ->
            snapshots.children.map { snapshot ->
                if (words.contains(snapshot.getValue(RememberWordModel::class.java)))
                    snapshot.getValue(RememberWordModel::class.java)?.copy(isCheck = true)
                else snapshot.getValue(RememberWordModel::class.java)
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun enableMultiSelect(word: RememberWordModel, words: List<RememberWordModel?>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (words.size > 1) {
                _selectedWords.update { words ->
                    words.toMutableList().apply {
                        add(word)
                    }.toList()
                }
            } else deleteWords(listOf(word))
        }
    }

    fun disableMultiSelect() {
        viewModelScope.launch(Dispatchers.IO) {
            unSelectAllWords()
        }
    }

    fun selectWord(word: RememberWordModel) {
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
        allWords: List<RememberWordModel?>,
        selectedWords: List<RememberWordModel?>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (allWords.size == selectedWords.size) unSelectAllWords()
            else selectAllWords(allWords = allWords)
        }
    }

    private fun selectAllWords(allWords: List<RememberWordModel?>) {
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

    fun deleteWords(models: List<RememberWordModel?>) {
        viewModelScope.launch(Dispatchers.IO) {
            rememberUserCase.deleteWords(models = models)
            unSelectAllWords()
        }
    }
}