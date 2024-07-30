package com.myapps.wtsacademy2.ApiWorks

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val BASE_URL = "https://wtsacademy.dedicateddevelopers.us/api/"
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build()

    fun <T> createService(service:Class<T>):T{
        return retrofit.create(service)
    }
}