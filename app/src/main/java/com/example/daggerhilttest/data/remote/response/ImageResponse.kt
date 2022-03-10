package com.example.daggerhilttest.data.remote.response

data class ImageResponse(
    val hits: List<ImageResult> ,
    val total :Int,
    val totalHits :Int
)