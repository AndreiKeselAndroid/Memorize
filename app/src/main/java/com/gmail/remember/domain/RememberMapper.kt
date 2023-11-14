package com.gmail.remember.domain

import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.InfoModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

fun GoogleSignInAccount.toModel(): ProfileModel = ProfileModel(
    idToken = this.idToken,
    displayName = this.displayName,
    familyName = this.familyName,
    givenName = this.givenName,
    photoUrl = this.photoUrl.toString()
)

fun ProfileModel.toModel(): InfoModel = InfoModel(
    displayName = this.displayName,
    photoUrl = this.photoUrl
)