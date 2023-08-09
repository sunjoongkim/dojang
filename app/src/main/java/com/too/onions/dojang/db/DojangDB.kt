package com.too.onions.dojang.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.too.onions.dojang.db.dao.ContentDao
import com.too.onions.dojang.db.dao.PageDao
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.Page

@Database(entities = [Content::class, Page::class], version = 1, exportSchema = false)
abstract class DojangDB : RoomDatabase() {

    abstract fun contentDao(): ContentDao
    abstract fun pageDao(): PageDao

    companion object {
        @Volatile
        private var Instance: DojangDB? = null

        fun getDatabase(context: Context): DojangDB {

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DojangDB::class.java, "dojang_db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}