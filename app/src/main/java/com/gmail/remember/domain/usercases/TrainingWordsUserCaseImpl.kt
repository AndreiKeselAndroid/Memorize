package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.models.WordModel
import javax.inject.Inject

internal class TrainingWordsUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : TrainingWordsUserCase{
    override suspend fun changeCountSuccessInWord(wordModel: WordModel) {
        rememberRepository.changeCountSuccessInWord(wordModel = wordModel)
    }

    override suspend fun changeCountErrorInWord(wordModel: WordModel) {
        rememberRepository.changeCountErrorInWord(wordModel = wordModel)
    }
}