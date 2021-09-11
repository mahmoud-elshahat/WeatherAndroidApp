package com.mahmoudelshahat.weatherandroidapp.ui.current_weather


import androidx.lifecycle.ViewModel

import androidx.lifecycle.liveData
import com.mahmoudelshahat.weatherandroidapp.data.network.utils.Resource
import com.mahmoudelshahat.weatherandroidapp.data.repository.WeatherNetworkRepository
import kotlinx.coroutines.Dispatchers


class CurrentWeatherViewModel(
    private val weatherNetworkRepository: WeatherNetworkRepository
) : ViewModel() {
    fun getCurrentWeather(city:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = weatherNetworkRepository.fetchCurrentWeather(city)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, msg = exception.message ?: "Error Occurred!"))
        }
    }
}