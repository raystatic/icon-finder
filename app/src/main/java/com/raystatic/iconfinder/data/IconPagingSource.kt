package com.raystatic.iconfinder.data

import androidx.paging.PagingSource
import com.raystatic.iconfinder.api.ApiService
import com.raystatic.iconfinder.data.models.Icon
import retrofit2.HttpException
import java.io.IOException

private const val ICON_STARTING_PAGE_INDEX = 1
class IconPagingSource(
    private val apiService:ApiService,
    private val query:String
):PagingSource<Int, Icon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Icon> {
        val position = params.key ?: ICON_STARTING_PAGE_INDEX

        return try {
            val response = apiService.searchIcon(query = query,count = params.loadSize)
            val icons = response.icons
            LoadResult.Page(
                data = icons,
                prevKey = if (position == ICON_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (icons.isEmpty()) null else position + 1
            )
        }catch (e:IOException){
            LoadResult.Error(e)
        }catch (e:HttpException){
            LoadResult.Error(e)
        }
    }
}