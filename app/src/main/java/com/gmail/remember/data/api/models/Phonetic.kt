package com.gmail.remember.data.api.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Phonetic(
    @SerialName("audio")
    val audio: String,
    @SerialName("license")
    val license: License,
    @SerialName("sourceUrl")
    val sourceUrl: String,
    @SerialName("text")
    val text: String
)