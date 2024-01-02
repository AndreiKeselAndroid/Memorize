package com.gmail.remember.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordModel(
    val id: Int = -1,
    val theme: String = "",
    val wordEng: String = "",
    val wordRu: String = "",
    val url: String = "",
    val countSuccess: Int = 0,
    val countError: Int = 0,
    val check: Boolean = false
) : Parcelable
