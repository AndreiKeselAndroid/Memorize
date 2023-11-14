package com.gmail.remember.data.api

import com.gmail.remember.data.api.models.dictionary.DictionaryRs
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    suspend fun getWord(@Path("word") word: String): DictionaryRs
}