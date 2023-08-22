package com.too.onions.dojang.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.db.data.User

@Dao
interface UserDao {

    @Query("SELECT * from users where email = :email")
    suspend fun get(email: String) : User?

    @Query("UPDATE users SET stamp = :stamp")
    suspend fun updateStamp(stamp: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE from users")
    suspend fun deleteAll()
}