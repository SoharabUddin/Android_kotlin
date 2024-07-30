package com.myapps.weatherlivedata.Views

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.myapps.weatherlivedata.R
import com.myapps.weatherlivedata.ViewModels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var search_input : TextInputEditText
    private lateinit var search_button :Button
    private lateinit var location_name :TextView
    private lateinit var localtime :TextView
    private lateinit var weather_icon :ImageView
    private lateinit var weather_condition :TextView
    private lateinit var temp_c :TextView
    private lateinit var feels_like :TextView
    private lateinit var wind :TextView
    private lateinit var humidity :TextView
    private lateinit var pressure :TextView
    private lateinit var errorText :TextView
    private lateinit var progressBar :ProgressBar
    private lateinit var relativeLayout :RelativeLayout

    // Declaration the ViewModel
    private lateinit var viewModel :MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        doObserve()
        search_button.setOnClickListener {  onSearchButtonClicked() }

    }

    private fun initViews(){
        search_input = findViewById(R.id.search_input)
        search_button = findViewById(R.id.search_button)
        location_name = findViewById(R.id.location_name)
        localtime = findViewById(R.id.localtime)
        weather_icon = findViewById(R.id.weather_icon)
        weather_condition = findViewById(R.id.weather_condition)
        temp_c = findViewById(R.id.temp_c)
        feels_like = findViewById(R.id.feels_like)
        wind = findViewById(R.id.wind)
        humidity = findViewById(R.id.humidity)
        pressure = findViewById(R.id.pressure)
        errorText = findViewById(R.id.errorText)
        progressBar = findViewById(R.id.progressBar)
        relativeLayout = findViewById(R.id.relativeLayout)
        //Initalization of the ViewModel
        viewModel = MainViewModel()
    }

    private fun onSearchButtonClicked(){
        val query = search_input.text.toString()
        search_input.text?.clear()
        if (query.isNullOrEmpty() or query.isNullOrBlank()){
            search_input.error = "Please give input"
        }
        else{
            viewModel.getWeatherData(query)
        }
    }

    private fun doObserve(){
        viewModel.weatherData.observe(this){
            location_name.text = it.location.name
            localtime.text = it.location.localtime
            weather_condition.text = it.current.condition.text
            temp_c.text = it.current.temp_c.toString()
            feels_like.text = "Feels like: ${it.current.feelslike_c} °C"
            wind.text = "Wind: ${it.current.wind_kph} kph"
            humidity.text = "Humidity: ${it.current.humidity}"
            pressure.text = "Pressure: ${it.current.pressure_mb}"
            setImage(it.current.condition.icon)

        }

        viewModel.isError.observe(this){
            if (it){
                relativeLayout.visibility = View.GONE
                errorText.text = viewModel.errorMessage
                errorText.visibility = View.VISIBLE
            }
            else{
                relativeLayout.visibility = View.VISIBLE
                errorText.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this){
            if (it){
                progressBar.visibility  = View.VISIBLE
            }
            else{
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun setImage(imageUrl: String?) {
        imageUrl.let { url ->
            Glide.with(applicationContext)
                .load("https:$url")
                .into(weather_icon)
        }
    }
}