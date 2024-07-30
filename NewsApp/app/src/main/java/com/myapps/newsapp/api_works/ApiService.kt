package com.myapps.newsapp.api_works

import com.myapps.newsapp.dataclass.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface ApiService {
    @GET("top-headlines")
    fun fetchData(@Query("country") country: String,@Query("category") category:String, @Query("apiKey") apiKey: String): Call<NewsData>
}