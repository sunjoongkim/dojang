package com.too.onions.dojang.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "pages")
data class Page (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val index: Int = 0,
    val emoji: String = "",
    val title: String = "",
    val friends: List<PageUser> = emptyList()
)

data class PageUser (
    val nickName: String,
    val stamp: String = ""
)
