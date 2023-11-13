package com.gmail.remember.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.gmail.remember.data.datastore.utils.profileDataStore
import com.gmail.remember.models.ProfileModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    @Singleton
    fun provideProfileDataStore(@ApplicationContext context: Context): DataStore<ProfileModel> =
        context.profileDataStore

}