package com.gmail.remember.models

data class ThemeModel(
    val name: String,
    val progress: Float,
    val isChecked: Boolean = false
)