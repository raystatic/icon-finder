package com.raystatic.iconfinder.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadedIcon(

    @PrimaryKey
    val path:String,

    val modifiedAt:String
)