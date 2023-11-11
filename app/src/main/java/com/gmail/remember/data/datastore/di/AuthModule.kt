package com.gmail.remember.data.datastore.di

import com.gmail.remember.data.datastore.models.auth.AuthPrefsModel
import com.gmail.remember.data.datastore.models.auth.AuthPrefsModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthModel(authPrefsModelImpl: AuthPrefsModelImpl): AuthPrefsModel
}