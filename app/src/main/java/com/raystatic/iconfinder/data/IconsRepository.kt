package com.raystatic.iconfinder.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.raystatic.iconfinder.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IconsRepository @Inject constructor(
    private val apiService: ApiService
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

}