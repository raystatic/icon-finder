package com.raystatic.iconfinder.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raystatic.iconfinder.data.models.DownloadedIcon

@Dao
interface DownloadsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIcon(icon:DownloadedIcon)

    @Query("SELECT * FROM downloads")
    fun getAllDownloads():LiveData<List<DownloadedIcon>>

    @Query("DELETE FROM downloads")
    suspend fun deleteAllDownloads()

}