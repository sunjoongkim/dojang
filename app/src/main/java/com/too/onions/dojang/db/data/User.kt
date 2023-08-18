package com.too.onions.dojang.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String = "",
    val nickname: String = "",
    val uuid: String = "",
    val stamp: String = ""
)
