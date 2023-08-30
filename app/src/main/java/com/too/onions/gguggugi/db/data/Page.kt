package com.too.onions.gguggugi.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pages")
data class Page (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val index: Int = 0,
    val emoji: String = "",
    val title: String = "",
    val friends: List<Friend> = emptyList()
)

data class Friend (
    val nickname: String,
    val stamp: String = ""
)
