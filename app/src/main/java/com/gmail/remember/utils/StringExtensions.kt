package com.gmail.remember.utils
fun String.toDayName(): String = when (this) {
    "mon" -> "Понедельник"
    "tue" -> "Вторник"
    "wed" -> "Среда"
    "thu" -> "Четверг"
    "fri" -> "Пятница"
    "sat" -> "Суббота"
    "sun" -> "Воскресенье"
    else -> ""
}