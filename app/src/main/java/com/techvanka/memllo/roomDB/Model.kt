package com.techvanka.memllo.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class  Model(
    @PrimaryKey val videoId:String,
    val videoTitle: String,
    val videoLink: String,
    val CreatorId:String,
    val CreatorName: String,
    val CreatorProfile:String,
    val videoLanguage:String
)