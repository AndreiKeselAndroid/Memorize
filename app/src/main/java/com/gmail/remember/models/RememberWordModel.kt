package com.gmail.remember.models

data class RememberWordModel(
    val wordEng: String = "",
    val wordRu: String = "",
    val url: String = "",
    val isCheck: Boolean = false
)
