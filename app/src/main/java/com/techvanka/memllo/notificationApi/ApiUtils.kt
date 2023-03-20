package com.techvanka.memllo.notificationApi

import com.techvanka.memllo.notificationApi.Constant.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiUtils {
    fun getInstance():ApiPost {
        var retrofit =Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiPost::class.java)
        return retrofit

    }
}