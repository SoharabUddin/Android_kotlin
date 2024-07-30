package com.myapps.imdb

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val Base_Url = "https://imdb146.p.rapidapi.com/"
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder().baseUrl(Base_Url).addConverterFactory(GsonConverterFactory.create()).client(
        client).build()

    fun <T> createService(service: Class<T>):T{
        return retrofit.create(service)
    }
}