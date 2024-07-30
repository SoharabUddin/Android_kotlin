package com.myapps.weatherlivedata.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.myapps.weatherlivedata.Models.CurrentWeatherResponse
import com.myapps.weatherlivedata.Models.ErrorResponse
import com.myapps.weatherlivedata.Networks.ServiceBuilder
import com.myapps.weatherlivedata.Networks.WeaterAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel():ViewModel() {
    private val _weatherData = MutableLiveData<CurrentWeatherResponse>()
    val weatherData: LiveData<CurrentWeatherResponse> get() = _weatherData

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    var errorMessage: String? = ""

    fun getWeatherData(city :String){
        _isLoading.value = true
        _isError.value = false
        val retrofit = ServiceBuilder.createService(WeaterAPI::class.java)

        retrofit.getCurrentWeather(city).enqueue(object:Callback<CurrentWeatherResponse>{
            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful || body!=null){
                    _weatherData.postValue(body)
                    _isLoading.value = false
                }
                else{
                    _isLoading.value = false
                    val errorModel = response.errorBody()?.string()?.let {
                        Gson().fromJson(it, ErrorResponse::class.java)
                    }
                    onError("${response.code()}: ${errorModel?.error?.message}")
                }
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                _isLoading.value = false
                onError("Failed: ${t.message}")
            }
        })
    }

    private fun onError(inputMessage: String?){
        errorMessage = if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
        else inputMessage

        _isError.value = true
    }

}