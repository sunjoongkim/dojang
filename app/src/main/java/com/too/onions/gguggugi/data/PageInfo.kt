package com.too.onions.gguggugi.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PageInfo (
    @SerializedName("idx") val idx: Long = 0,
    @SerializedName("ownerIdx") val ownerIdx: Long = 0,
    @SerializedName("type") val type: String = "",
    @SerializedName("symbol") val emoji: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("maxParticipants") val maxParticipants: Int = -1,
    @SerializedName("maxMissions") val maxMissions: Int = -1,
    @SerializedName("myStampType") val stampType: String? = "",
    @SerializedName("myStampContent") val stamp: String? = "",
) : Parcelable

