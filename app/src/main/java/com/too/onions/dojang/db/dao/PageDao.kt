package com.too.onions.dojang.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.too.onions.dojang.db.data.Page

@Dao
interface PageDao {

    @Query("SELECT * from pages")
    suspend fun getAll() : List<Page>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(page: Page)

    @Update
    suspend fun update(page: Page)

    @Delete
    suspend fun delete(page: Page)

    @Query("DELETE from pages")
    suspend fun deleteAll()
}