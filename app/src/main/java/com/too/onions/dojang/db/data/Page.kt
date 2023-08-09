package com.too.onions.dojang.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pages")
data class Page (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val emoji: String = "",
    val title: String = "",
    val friends: String = ""
)
