package com.techvanka.memllo.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Model::class], version = 1)
abstract class VideoStorageHope : RoomDatabase() {
    abstract fun videoDoa(): Dao
}