package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.data.api.models.DictionaryRs
import com.gmail.remember.models.RememberWordModel
import com.google.android.gms.tasks.Task
import javax.inject.Inject

internal class AddWordsUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : AddWordsUserCase {
    override fun addWord(model: RememberWordModel): Task<Void> =
        rememberRepository.addWord(model = model)

    override suspend fun getWordFromDictionary(word: String): DictionaryRs? =
        rememberRepository.getWordFromDictionary(word = word)
}