package com.too.onions.gguggugi.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Participant (
    @SerializedName("userIdx") val userIdx: Long = 0,
    @SerializedName("username") val username: String = "",
    @SerializedName("stampType") val stampType: String? = "",
    @SerializedName("stampContent") val stampContent: String? = "",
    @SerializedName("isOwner") val isOwner: Boolean = false
) : Parcelable
