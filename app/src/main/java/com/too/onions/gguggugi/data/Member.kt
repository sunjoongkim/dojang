package com.too.onions.gguggugi.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Member (
    @SerializedName("userIdx") val userIdx: Long = 0,
    @SerializedName("username") val username: String = "",
    @SerializedName("stampType") val stampType: String? = "",
    @SerializedName("stampContent") val stampContent: String? = "",
    @SerializedName("isOwner") val isOwner: Boolean = false
) : Parcelable
