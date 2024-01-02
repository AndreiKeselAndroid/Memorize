package com.gmail.remember.domain.usercases

import com.gmail.remember.models.WordModel

interface TrainingWordsUserCase {
    suspend fun changeCountSuccessInWord(wordModel: WordModel)
    suspend fun changeCountErrorInWord(wordModel: WordModel)
}