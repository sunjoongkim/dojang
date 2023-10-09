package com.too.onions.gguggugi.db.data

import com.google.gson.annotations.SerializedName

data class Auth (
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("newUserYN") val isNew: String
)