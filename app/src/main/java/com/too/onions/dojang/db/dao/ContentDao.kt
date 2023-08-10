package com.too.onions.dojang.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.too.onions.dojang.db.data.Content

@Dao
interface ContentDao {

    @Query("SELECT * from contents where pageId = :pageId")
    suspend fun getAll(pageId: Long) : List<Content>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(content: Content)

    @Update
    suspend fun update(content: Content)

    @Delete
    suspend fun delete(content: Content)

    @Query("DELETE from contents where pageId = :pageId")
    suspend fun deleteAll(pageId: Long)
}