package com.techvanka.memllo.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<Model>>

    @Insert
    fun insertUser(user: Model)

    @Delete
    fun deleteUser(user: Model)
}