package com.ozanyazici.artbooktest.api

import com.ozanyazici.artbooktest.model.ImageResponse
import com.ozanyazici.artbooktest.util.Util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("/api/")
    suspend fun imageSearch(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = API_KEY
    ): Response<ImageResponse>
}