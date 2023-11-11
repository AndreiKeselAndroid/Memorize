package com.gmail.remember.models

data class RememberWordModel(
    val wordEng: String = "Hello",
    val wordRu: String = "Привет",
    val url: String = "https://ssl.gstatic.com/dictionary/static/sounds/20200429/hello--_gb_1.mp3",
    val isCheck: Boolean = false
)
