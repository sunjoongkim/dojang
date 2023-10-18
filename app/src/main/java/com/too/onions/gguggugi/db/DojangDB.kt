package com.too.onions.gguggugi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.too.onions.gguggugi.db.dao.ContentDao
import com.too.onions.gguggugi.db.dao.PageDao
import com.too.onions.gguggugi.db.dao.UserDao
import com.too.onions.gguggugi.db.data.Content
import com.too.onions.gguggugi.db.data.Page
import com.too.onions.gguggugi.db.data.User
import com.too.onions.gguggugi.db.data.converter.Converters

@Database(entities = [Page::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DojangDB : RoomDatabase() {

    abstract fun contentDao(): ContentDao
    abstract fun pageDao(): PageDao
    abstract fun userDao(): UserDao

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