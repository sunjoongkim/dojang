package com.too.onions.gguggugi.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InitPage (
    @SerializedName("participantList") val participantList: List<Participant>? = emptyList(),
    @SerializedName("missionList") val missionList: List<Content>? = emptyList(),
    @SerializedName("pageList") val pageList: List<Page>? = emptyList(),
    @SerializedName("firstPageInfo") val firstPageInfo: Page? = null
) : Parcelable
