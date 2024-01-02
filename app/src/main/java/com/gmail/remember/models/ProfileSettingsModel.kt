package com.gmail.remember.models

import com.gmail.remember.utils.decrypt
import com.gmail.remember.utils.encrypt
import com.gmail.remember.utils.toDay
import java.util.Calendar

data class ProfileSettingsModel(
    val idToken: String? = "",
    val familyName: String? = "",
    val givenName: String? = "",
    val displayName: String? = "",
    val photoUrl: String? = "",
    val countSuccess: String = "5",
    val timeFrom: String = "9",
    val timeTo: String = "21",
    val allDays: Boolean = true,
    val colorTheme: String = "system",
    val remember: Boolean = false,
    val days: HashMap<String, DayModel> = hashMapOf(),
    val theme: String = ""
)

fun ProfileSettingsModel.isShowNotification(): Boolean =
    if (this.allDays) remember && Calendar.getInstance()
        .get(Calendar.HOUR_OF_DAY) in this.timeFrom.toInt() until this.timeTo.toInt()
            && theme.isNotEmpty()
    else remember && this.days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        .toDay()]?.check == true &&
            Calendar.getInstance()
                .get(Calendar.HOUR_OF_DAY) in this.timeFrom.toInt()until this.timeTo.toInt()
            && theme.isNotEmpty()

fun ProfileSettingsModel.encrypt(): ProfileSettingsModel = this.copy(
    idToken = this.idToken?.encrypt(),
    familyName = this.familyName?.encrypt(),
    givenName = this.givenName?.encrypt(),
    displayName = this.displayName?.encrypt(),
    photoUrl = this.photoUrl?.encrypt(),
    countSuccess = this.countSuccess.encrypt(),
    timeFrom = this.timeFrom.encrypt(),
    timeTo = this.timeTo.encrypt(),
    colorTheme = this.colorTheme.encrypt(),
    theme = this.theme.encrypt()
)

fun ProfileSettingsModel.decrypt(): ProfileSettingsModel = this.copy(
    idToken = this.idToken?.decrypt(),
    familyName = this.familyName?.decrypt(),
    givenName = this.givenName?.decrypt(),
    displayName = this.displayName?.decrypt(),
    photoUrl = this.photoUrl?.decrypt(),
    countSuccess = this.countSuccess.decrypt(),
    timeFrom = this.timeFrom.decrypt(),
    timeTo = this.timeTo.decrypt(),
    colorTheme = this.colorTheme.decrypt(),
    theme = this.theme.decrypt()
)
