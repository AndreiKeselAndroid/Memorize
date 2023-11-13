package com.gmail.remember.data.creator

import retrofit2.Retrofit
import javax.inject.Inject
import kotlin.reflect.KClass

internal class ServiceCreatorImpl @Inject constructor(private val retrofit: Retrofit) :
    ServiceCreator {
    override fun <T : Any> create(service: KClass<T>): T = retrofit.create(service.java)
}