package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.data.api.models.dictionary.DictionaryRs
import com.gmail.remember.models.WordModel
import com.google.android.gms.tasks.Task
import javax.inject.Inject

internal class AddWordsUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : AddWordsUserCase {
    override suspend fun addWord(model: WordModel, childName: String): Task<Void> =
        rememberRepository.addWord(model = model, childName = childName)

    override suspend fun getWordFromDictionary(word: String): DictionaryRs? =
        rememberRepository.getWordFromDictionary(word = word)
}