package com.raystatic.iconfinder.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.raystatic.iconfinder.api.ApiService
import com.raystatic.iconfinder.data.models.DownloadedIcon
import com.raystatic.iconfinder.room.DownloadsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IconsRepository @Inject constructor(
    private val apiService: ApiService,
    private val downloadsDao: DownloadsDao
) {

    fun getSearchResult(query:String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                IconPagingSource(apiService, query)
            }
        ).liveData

    suspend fun downloadIcon(url:String) = apiService.downloadFile(url)

    suspend fun insertDownloadedIcon(downloadedIcon: DownloadedIcon) = downloadsDao.insertIcon(downloadedIcon)

    suspend fun deleteAllDownload() = downloadsDao.deleteAllDownloads()

    fun getAllDownloads() = downloadsDao.getAllDownloads()

}