package com.gmail.remember.data.api.models.dictionary


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meaning(
    @SerialName("antonyms")
    val antonyms: List<String>,
    @SerialName("definitions")
    val definitions: List<Definition>,
    @SerialName("partOfSpeech")
    val partOfSpeech: String,
    @SerialName("synonyms")
    val synonyms: List<String>
)