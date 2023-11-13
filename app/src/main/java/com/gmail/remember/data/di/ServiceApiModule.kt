package com.gmail.remember.data.di

import com.gmail.remember.data.creator.ServiceCreator
import com.gmail.remember.data.creator.ServiceCreatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ServiceApiModule {

    @Binds
    @Singleton
    abstract fun bindServiceCreator(serviceCreatorImpl: ServiceCreatorImpl): ServiceCreator
}