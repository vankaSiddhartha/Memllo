package com.techvanka.memllo.roomDB

import android.content.Context
import androidx.room.Room


object StoreObject {
    private var instance: VideoStorageHope? = null

    fun getInstance(context: Context): VideoStorageHope {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }

    private fun buildDatabase(context: Context): VideoStorageHope {
        return Room.databaseBuilder(
            context.applicationContext,
            VideoStorageHope::class.java, "Data"
        )
            .build()
    }
}