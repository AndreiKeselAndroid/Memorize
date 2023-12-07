package com.gmail.remember.data.api.models.dictionary


import com.google.gson.annotations.SerializedName

data class DictionaryRsItem(
    @SerializedName("license")
    val license: License?,
    @SerializedName("meanings")
    val meanings: List<Meaning>,
    @SerializedName("phonetics")
    val phonetics: List<Phonetic?>,
    @SerializedName("sourceUrls")
    val sourceUrls: List<String>,
    @SerializedName("word")
    val word: String
)