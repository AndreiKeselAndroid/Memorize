package com.gmail.remember.data.datastore.di

import com.gmail.remember.data.datastore.models.auth.ProfilePrefsModel
import com.gmail.remember.data.datastore.models.auth.ProfilePrefsModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindProfileModel(profilePrefsModelImpl: ProfilePrefsModelImpl): ProfilePrefsModel
}