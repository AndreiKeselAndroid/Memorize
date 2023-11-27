package com.gmail.remember.domain.usercases

import com.gmail.remember.data.api.models.dictionary.DictionaryRs
import com.gmail.remember.models.WordModel
import com.google.android.gms.tasks.Task

interface AddWordsUserCase {
    suspend fun addWord(model: WordModel, childName: String): Task<Void>
    suspend fun getWordFromDictionary(word: String): DictionaryRs?
}