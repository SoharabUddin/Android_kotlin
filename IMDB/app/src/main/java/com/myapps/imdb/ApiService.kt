package com.myapps.imdb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("v1/find")
    fun find(
        @Header("x-rapidapi-key") apiToken:String = "88ae60e931msh99692eeec2af955p1a2ca6jsn191939f00c11",
        @Header("x-rapidapi-host") apiHost:String = "imdb146.p.rapidapi.com",
        @Query("query") query: String
    ): Call<FindResponse>
}