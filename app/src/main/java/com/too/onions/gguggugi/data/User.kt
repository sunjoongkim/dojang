package com.too.onions.gguggugi.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    @SerializedName("idx") val id: Long = 0,
    @SerializedName("email") val email: String = "",
    @SerializedName("username") val nickname: String = "",
    @SerializedName("profileImgContent") val stamp: String = ""
) : Parcelable
