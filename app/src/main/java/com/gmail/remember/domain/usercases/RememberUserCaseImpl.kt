package com.gmail.remember.domain.usercases

import com.gmail.remember.data.RememberRepository
import javax.inject.Inject

internal class RememberUserCaseImpl @Inject constructor(
    private val rememberRepository: RememberRepository
) : RememberUserCase {
}