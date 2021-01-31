package com.raystatic.iconfinder.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raystatic.iconfinder.data.models.DownloadedIcon

@Database(
    entities = [DownloadedIcon::class],
    version = 1
)
abstract class IconDB: RoomDatabase() {

    abstract fun getDownloadsDao():DownloadsDao

}