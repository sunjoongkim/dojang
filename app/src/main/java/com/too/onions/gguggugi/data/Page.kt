package com.too.onions.gguggugi.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Page (
    @SerializedName("participantList") val memberList: List<Member> = emptyList(),
    @SerializedName("missionList") val contentList: List<Content>? = emptyList(),
    @SerializedName("pageInfo") val pageInfo: PageInfo,
) : Parcelable

