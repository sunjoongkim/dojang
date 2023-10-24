package com.too.onions.gguggugi.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InitPage (
    @SerializedName("participantList") val memberList: List<Member> = emptyList(),
    @SerializedName("missionList") val contentList: List<Content>? = emptyList(),
    @SerializedName("pageList") val pageList: List<Page>? = emptyList(),
    @SerializedName("firstPageInfo") val firstPageInfo: Page? = null
) : Parcelable
