package com.example.base.network

import okhttp3.ResponseBody
import retrofit2.http.GET

interface NetworkService {
    @GET("tin-moi-nhat.rss")
    suspend fun getNews(): ResponseBody
}
