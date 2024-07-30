package com.myapps.moviesapp.apiWorks


import com.myapps.moviesapp.dataClass.MoviesData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers(
        "X-RapidAPI-Key:88ae60e931msh99692eeec2af955p1a2ca6jsn191939f00c11",
        "X-RapidAPI-Host:moviesdatabase.p.rapidapi.com"
    )
    @GET("titles/x/upcoming")
    fun fetchData(): Call<MoviesData>
}