package com.gmail.remember.domain

import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.FRIDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.MONDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.SATURDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.SUNDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.THURSDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.TUESDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.WEDNESDAY
import com.gmail.remember.models.DayModel
import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.ProfileSettingsModel
import com.gmail.remember.models.ThemeModel
import com.gmail.remember.models.WordModel
import com.gmail.remember.utils.toDayName
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DataSnapshot

fun GoogleSignInAccount.toModel(): ProfileModel = ProfileModel(
    idToken = this.idToken,
    displayName = this.displayName,
    familyName = this.familyName,
    givenName = this.givenName,
    photoUrl = this.photoUrl.toString()
)

fun DataSnapshot.toModel(count: Int): ThemeModel = ThemeModel(
    name = this.key ?: "",
    progress = this.toProgress(count = count)
)

private fun DataSnapshot.toProgress(count: Int): Float {
    var sumCount = 0f
    var size = 0
    this.children.forEach {
        sumCount += it.getValue(WordModel::class.java)?.countSuccess?.toFloat() ?: 0f
        size += 1
    }
    return try {
        if (size > 0 && count > 0) sumCount / (size * count) else 0f
    } catch (e: Exception) {
        0f
    }
}

fun ProfileModel.toModel(): ProfileSettingsModel = ProfileSettingsModel(
    displayName = this.displayName,
    photoUrl = this.photoUrl,
    days = hashMapOf(
        MONDAY.toDayName() to DayModel(name = MONDAY.toDayName(), check = false, id = 0),
        TUESDAY.toDayName() to DayModel(name = TUESDAY.toDayName(), check = false, id = 1),
        WEDNESDAY.toDayName() to DayModel(name = WEDNESDAY.toDayName(), check = false, id = 2),
        THURSDAY.toDayName() to DayModel(name = THURSDAY.toDayName(), check = false, id = 3),
        FRIDAY.toDayName() to DayModel(name = FRIDAY.toDayName(), check = false, id = 4),
        SATURDAY.toDayName() to DayModel(name = SATURDAY.toDayName(), check = false, id = 5),
        SUNDAY.toDayName() to DayModel(name = SUNDAY.toDayName(), check = false, id = 6),
    )
)