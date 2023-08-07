package com.too.onions.dojang.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.too.onions.dojang.db.dao.ContentDao
import com.too.onions.dojang.db.data.Content

@Database(entities = [Content::class], version = 1, exportSchema = false)
abstract class ContentDB : RoomDatabase() {

    abstract fun contentDao(): ContentDao

    companion object {
        @Volatile
        private var Instance: ContentDB? = null

        fun getDatabase(context: Context): ContentDB {

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ContentDB::class.java, "dojang_db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}