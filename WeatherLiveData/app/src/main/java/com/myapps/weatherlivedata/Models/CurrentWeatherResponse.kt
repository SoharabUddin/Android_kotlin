package com.myapps.weatherlivedata.Models
data class CurrentWeatherResponse(
    val location : Location,
    val current : Current
)
data class Location(
    val name : String = "",
    val region : String = "",
    val country : String = "",
    val tz_id : String = "",
    val localtime : String = "",
    val lat : Float = 0.0f,
    val lon : Float = 0.0f,
    val localtime_epoch :Long = 0L
)
data class Current(
    val last_updated : String = "",
    val temp_c : Float = 0.0f,
    val temp_f : Float = 0.0f,
    val is_day : Int = 0,
    val wind_mph : Float = 0.0f,
    val wind_kph : Float = 0.0f,
    val wind_degree : Int = 0,
    val wind_dir : String ="",
    val feelslike_c : Float =0.0f,
    val pressure_mb : Float = 0.0f,
    val humidity : Int = 0,
    val cloud : Int = 0,
    val condition : Condition
)
data class Condition(
    val text :String ="",
    val icon :String ="",
)

