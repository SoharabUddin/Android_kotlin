package com.myapps.weatherlivedata.Networks

import com.myapps.weatherlivedata.Models.CurrentWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeaterAPI {
    @GET("v1/current.json")
    fun getCurrentWeather(
        @Query("q") query: String,
        @Query("key") apiKey :String = "4d8450c0d22b466798b72749242707",
        @Query("lang") language:String = "bn"
    ) : Call<CurrentWeatherResponse>


}