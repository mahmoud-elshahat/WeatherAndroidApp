package com.mahmoudelshahat.weatherandroidapp.ui.current_weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoudelshahat.weatherandroidapp.data.repository.WeatherNetworkRepository

@Suppress("UNCHECKED_CAST")
class CurrentWeatherModelFactory(private  val weatherNetworkRepository: WeatherNetworkRepository)
    :ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherNetworkRepository) as T
    }
}