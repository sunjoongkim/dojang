package com.too.onions.gguggugi.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content (
    @SerializedName("idx") val idx: Long = 0,
    @SerializedName("ownerIdx") val ownerIdx: Long = 0,
    @SerializedName("userIdx") val userIdx: Long = 0,
    @SerializedName("pageIdx") val pageIdx: Long = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("bgType") val bgType: String = "",
    @SerializedName("bgImgFile") val bgImgFile: String? = "",
    @SerializedName("bgContent") val bgContent: String = "",
    @SerializedName("fileIdx") val fileIdx: Long = 0,
    @SerializedName("description") val description: String = "",
    @SerializedName("address") val address: String? = "",
    @SerializedName("order") val order: Long = 0,
    @SerializedName("stampList") val stampList: List<Stamp> = listOf(),
) : Parcelable
