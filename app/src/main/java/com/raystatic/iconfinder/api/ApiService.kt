package com.raystatic.iconfinder.api

import com.raystatic.iconfinder.BuildConfig
import com.raystatic.iconfinder.data.models.IconSearchResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    companion object{
        const val BASE_URL = "https://api.iconfinder.com/v4/"
        const val API_KEY = BuildConfig.ICON_FINDER_ACCESS_KEY
    }

    @Headers("Accept-Version: v1","Authorization: Bearer $API_KEY")
    @GET("icons/search")
    suspend fun searchIcon(
        @Query("query") query:String,
        @Query("count") count:Int
    ): IconSearchResponse

    @Headers("Accept-Version: v1","Authorization: Bearer $API_KEY")
    @GET
    suspend fun downloadFile(
        @Url url:String
    ):Response<ResponseBody>

}