package com.gmail.remember.models

data class ProgressModel(
    val name: String = "",
    val progress: Float = 0f,
    val countSuccess: Int = 0,
    val countError: Int = 0,
    val countLearnt: Int = 0,
    val size: Int = 0
)

fun ProgressModel?.isShowProgress(isRemember: Boolean): Boolean = this?.progress != null &&
        this.name.isNotEmpty() &&
        this.progress != 0f &&
        isRemember
