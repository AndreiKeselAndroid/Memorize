package com.gmail.remember.data.api.models.dictionary


import com.google.gson.annotations.SerializedName

data class License(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)