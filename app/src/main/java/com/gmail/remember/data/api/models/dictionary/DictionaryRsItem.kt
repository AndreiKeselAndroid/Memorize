package com.gmail.remember.data.api.models.dictionary


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictionaryRsItem(
    @SerialName("license")
    val license: License?,
    @SerialName("meanings")
    val meanings: List<Meaning>,
    @SerialName("phonetics")
    val phonetics: List<Phonetic?>,
    @SerialName("sourceUrls")
    val sourceUrls: List<String>,
    @SerialName("word")
    val word: String
)