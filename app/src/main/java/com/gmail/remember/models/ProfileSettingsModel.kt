package com.gmail.remember.models

data class ProfileSettingsModel(
    val displayName: String? = "",
    val photoUrl: String? = "",
    val countSuccess: String = "5",
    val timeFrom: String = "09:00",
    val timeTo: String = "21:00",
    val allDays: Boolean = true,
    val remember: Boolean = false,
    val days: HashMap<String, DayModel> = hashMapOf()
)
