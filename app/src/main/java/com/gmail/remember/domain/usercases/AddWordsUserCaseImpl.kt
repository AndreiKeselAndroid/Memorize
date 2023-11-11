package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import javax.inject.Inject

internal class AddWordsUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : AddWordsUserCase {

}