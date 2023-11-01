package com.too.onions.gguggugi.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataResponse(
    @SerializedName("data") val data: String? = "",
    @SerializedName("message") val message: String? = "",
) : Parcelable
