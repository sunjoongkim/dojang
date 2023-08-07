package com.too.onions.dojang.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contents")
data class Content (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val color: Int,
    val imageUri: String?,
    val title: String,
    val description: String,
    val address: String
)
