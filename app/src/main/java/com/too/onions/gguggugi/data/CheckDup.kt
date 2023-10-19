package com.too.onions.gguggugi.data

import com.google.gson.annotations.SerializedName

data class CheckDup (
    @SerializedName("message") val message: String,
    @SerializedName("duplicated") val duplicated: String,
)