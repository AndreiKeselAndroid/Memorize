package com.gmail.remember.domain.usercases

import com.gmail.remember.data.api.models.DictionaryRs
import com.gmail.remember.models.RememberWordModel
import com.google.android.gms.tasks.Task

interface AddWordsUserCase {
    fun addWord(model: RememberWordModel): Task<Void>
    suspend fun getWordFromDictionary(word: String): DictionaryRs?
}