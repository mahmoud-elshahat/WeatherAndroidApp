package com.mahmoudelshahat.weatherandroidapp.ui.city_forecast

import androidx.lifecycle.ViewModel

import androidx.lifecycle.liveData
import com.mahmoudelshahat.weatherandroidapp.data.network.utils.Resource
import com.mahmoudelshahat.weatherandroidapp.data.repository.WeatherNetworkRepository
import kotlinx.coroutines.Dispatchers


class ForecastViewModel(
    private val weatherNetworkRepository: WeatherNetworkRepository
) : ViewModel() {
    fun getForecastFiveDays(city:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = weatherNetworkRepository.fetchForecastFiveDays(city)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, msg = exception.message ?: "Error Occurred!"))
        }
    }

}