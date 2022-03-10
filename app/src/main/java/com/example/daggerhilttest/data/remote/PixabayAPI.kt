package com.example.daggerhilttest.data.remote

import com.example.daggerhilttest.BuildConfig
import com.example.daggerhilttest.data.remote.response.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") q: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>

}