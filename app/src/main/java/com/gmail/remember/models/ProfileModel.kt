package com.gmail.remember.models

import kotlinx.serialization.Serializable

@Serializable
data class ProfileModel(
    val idToken: String? = "",
    val displayName: String? = "",
    val familyName: String? = "",
    val givenName: String? = "",
    val photoUrl: String? = "",
)