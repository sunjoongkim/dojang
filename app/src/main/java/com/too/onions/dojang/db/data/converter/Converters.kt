package com.too.onions.dojang.db.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.too.onions.dojang.db.data.PageUser
import com.too.onions.dojang.db.data.Stamp

class Converters {

    @TypeConverter
    fun fromUserList(userList: List<PageUser>): String {
        return Gson().toJson(userList)
    }

    @TypeConverter
    fun toUserList(userListString: String): List<PageUser> {
        val type = object : TypeToken<List<PageUser>>() {}.type
        return Gson().fromJson(userListString, type)
    }
    @TypeConverter
    fun fromStampList(stampList: List<Stamp>): String {
        return Gson().toJson(stampList)
    }

    @TypeConverter
    fun toStampList(stampListString: String): List<Stamp> {
        val type = object : TypeToken<List<Stamp>>() {}.type
        return Gson().fromJson(stampListString, type)
    }
    @TypeConverter
    fun stringToList(value: String): List<String> = value.split((",")).map { it.trim() }

    @TypeConverter
    fun listToString(list: List<String>): String = list.joinToString {","}
}