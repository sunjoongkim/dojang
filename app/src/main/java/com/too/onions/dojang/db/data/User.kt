package com.too.onions.dojang.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @PrimaryKey
    val email: String = "",
    @PrimaryKey
    val nickname: String = "",
    @PrimaryKey
    val uuid: String = "",
    val stamp: String = ""
)
