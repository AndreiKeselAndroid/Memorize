package com.gmail.remember.di

import com.gmail.remember.data.RememberRepository
import com.gmail.remember.data.RememberRepositoryImpl
import com.gmail.remember.domain.usercases.AddWordsUserCase
import com.gmail.remember.domain.usercases.AddWordsUserCaseImpl
import com.gmail.remember.domain.usercases.AuthUserCase
import com.gmail.remember.domain.usercases.AuthUserCaseImpl
import com.gmail.remember.domain.usercases.WordsUserCase
import com.gmail.remember.domain.usercases.WordsUserCaseImpl
import com.gmail.remember.domain.usercases.ProfileUserCase
import com.gmail.remember.domain.usercases.ProfileUserCaseImpl
import com.gmail.remember.domain.usercases.MainUserCase
import com.gmail.remember.domain.usercases.MainUserCaseImpl
import com.gmail.remember.domain.usercases.TrainingWordsUserCase
import com.gmail.remember.domain.usercases.TrainingWordsUserCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RememberModule {
    @Binds
    @Singleton
    abstract fun bindRememberRepository(rememberRepositoryImpl: RememberRepositoryImpl): RememberRepository

    @Binds
    @Singleton
    abstract fun bindAuthUserCase(authUserCaseImpl: AuthUserCaseImpl): AuthUserCase

    @Binds
    @Singleton
    abstract fun bindWordsUserCase(wordsUserCaseImpl: WordsUserCaseImpl): WordsUserCase

    @Binds
    @Singleton
    abstract fun bindMainUserCase(mainUserCaseImpl: MainUserCaseImpl): MainUserCase

    @Binds
    @Singleton
    abstract fun bindAddWordsUserCase(addWordsUserCase: AddWordsUserCaseImpl): AddWordsUserCase

    @Binds
    @Singleton
    abstract fun bindProfileUserCase(profileUserCaseImpl: ProfileUserCaseImpl): ProfileUserCase

    @Binds
    @Singleton
    abstract fun bindTrainingWordsUserCase(trainingWordsUserCaseImpl: TrainingWordsUserCaseImpl): TrainingWordsUserCase
}