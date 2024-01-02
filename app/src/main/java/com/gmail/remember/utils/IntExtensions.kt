package com.gmail.remember.utils

import androidx.core.text.util.LocalePreferences
import java.util.Calendar

fun Int.toDay(): String = when (this) {
    Calendar.MONDAY -> {
        LocalePreferences.FirstDayOfWeek.MONDAY.toDayName()
    }

    Calendar.TUESDAY -> {
        LocalePreferences.FirstDayOfWeek.TUESDAY.toDayName()
    }

    Calendar.WEDNESDAY -> {
        LocalePreferences.FirstDayOfWeek.WEDNESDAY.toDayName()
    }

    Calendar.THURSDAY -> {
        LocalePreferences.FirstDayOfWeek.THURSDAY.toDayName()
    }

    Calendar.FRIDAY -> {
        LocalePreferences.FirstDayOfWeek.FRIDAY.toDayName()
    }

    Calendar.SATURDAY -> {
        LocalePreferences.FirstDayOfWeek.SATURDAY.toDayName()
    }

    Calendar.SUNDAY -> {
        LocalePreferences.FirstDayOfWeek.SUNDAY.toDayName()
    }

    else -> ""
}