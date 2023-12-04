package com.too.onions.gguggugi.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InitPage (
    @SerializedName("participantList") val memberList: List<Friend> = emptyList(),
    @SerializedName("missionList") val contentList: List<Content>? = emptyList(),
    @SerializedName("pageList") val pageList: List<PageInfo>? = emptyList(),
    @SerializedName("firstPageInfo") val firstPageInfo: PageInfo? = null
) : Parcelable
