package com.gmail.remember.models

import kotlinx.serialization.Serializable

@Serializable
data class DayModel(
    val id: Int = -1,
    val name: String = "",
    val check: Boolean = false
)