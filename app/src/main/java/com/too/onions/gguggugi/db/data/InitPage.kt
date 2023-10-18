package com.too.onions.gguggugi.db.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InitPage (
    @SerializedName("participantList") val participantList: List<String>? = emptyList(),
    @SerializedName("missionList") val missionList: List<String>? = emptyList(),
    @SerializedName("pageList") val pageList: List<String> = emptyList(),
    @SerializedName("firstPageInfo") val firstPageInfo: List<String>? = emptyList()
) : Parcelable
