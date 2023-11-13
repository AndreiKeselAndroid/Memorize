package com.gmail.remember.utils

import com.gmail.remember.models.ProfileModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

fun GoogleSignInAccount.toModel(): ProfileModel = ProfileModel(
    idToken = this.idToken,
    displayName = this.displayName,
    familyName = this.familyName,
    givenName = this.givenName,
    photoUrl = this.photoUrl.toString()
)