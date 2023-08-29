package com.too.onions.dojang.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contents")
data class Content (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pageId: Long = 0L,
    val color: Int = 0,
    val imageUri: String?,
    val title: String = "",
    val description: String = "",
    val address: String = "",
    val stamps: List<Stamp> = emptyList()
)

data class Stamp (
    val user: String,
    val stamp: String,
    val x: Int,
    val y: Int
)