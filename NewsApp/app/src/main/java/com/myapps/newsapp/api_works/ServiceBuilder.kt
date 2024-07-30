package com.myapps.newsapp.api_works

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {

    private val BASE_URL = "https://newsapi.org/v2/"
    private val okHttpClient = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()

    fun <T> createService(service:Class<T>):T{
        return retrofit.create(service)
    }
}