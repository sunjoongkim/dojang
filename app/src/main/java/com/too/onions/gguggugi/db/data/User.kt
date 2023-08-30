package com.too.onions.gguggugi.db.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String = "",
    val nickname: String = "",
    val uuid: String = "",
    val stamp: String = ""
) : Parcelable
