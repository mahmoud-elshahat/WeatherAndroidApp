package com.mahmoudelshahat.weatherandroidapp.data.network.api

import com.mahmoudelshahat.weatherandroidapp.data.model.CurrentWeather
import com.mahmoudelshahat.weatherandroidapp.data.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend  fun getCurrentWeatherByCityName(
        @Query("q")city:String,
        @Query("units")unitType:String
    ):CurrentWeather

    @GET("forecast")
    suspend  fun getForecastByCityName(
        @Query("q")city:String,
        @Query("units")unitType:String
    ):ForecastResponse
}