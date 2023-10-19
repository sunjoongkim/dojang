package com.too.onions.gguggugi.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Stamp (
    @SerializedName("idx") val idx: Long = 0,
    @SerializedName("userIdx") val userIdx: Long = 0,
    @SerializedName("missionIdx") val missionIdx: Long = 0,
    @SerializedName("stampType") val stampType: String = "",
    @SerializedName("stampContent") val stampContent: String = "",
    @SerializedName("locX") val locX: Int = 0,
    @SerializedName("locY") val locY: Int = 0,
    @SerializedName("rotation") val rotation: Int = 0,
    val stamp: String,
    val x: Int,
    val y: Int
) : Parcelable