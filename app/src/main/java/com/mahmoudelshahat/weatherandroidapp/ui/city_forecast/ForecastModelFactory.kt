package com.mahmoudelshahat.weatherandroidapp.ui.city_forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoudelshahat.weatherandroidapp.data.repository.WeatherNetworkRepository

@Suppress("UNCHECKED_CAST")
class ForecastModelFactory(private  val weatherNetworkRepository: WeatherNetworkRepository)
    :ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForecastViewModel(weatherNetworkRepository) as T
    }
}