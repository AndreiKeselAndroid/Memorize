package com.gmail.remember.data.creator

import kotlin.reflect.KClass

interface ServiceCreator {
    fun <T : Any> create(service: KClass<T>): T
}