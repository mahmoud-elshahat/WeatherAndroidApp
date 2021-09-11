package com.mahmoudelshahat.weatherandroidapp.data.repository

import com.mahmoudelshahat.weatherandroidapp.data.network.api.WeatherApiService
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.PREFERRED_UNIT_TYPE


class WeatherNetworkRepository(
    private val apiService: WeatherApiService
) {
    suspend fun fetchCurrentWeather(city:String) =
        apiService.getCurrentWeatherByCityName(city,PREFERRED_UNIT_TYPE)

    suspend fun fetchForecastFiveDays(city:String,unit:String=PREFERRED_UNIT_TYPE) =
        apiService.getForecastByCityName(city,unit)
}