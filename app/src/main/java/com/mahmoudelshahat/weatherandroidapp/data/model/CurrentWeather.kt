package com.mahmoudelshahat.weatherandroidapp.data.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("weather")
    val weatherList: List<Weather>,
    @SerializedName("main")
    val mainObject: Main,
    val wind: Wind,
    @SerializedName("name")

    val city: String,
)

data class Weather(
    @SerializedName("main")
    val title:String,
    val description:String,
    @SerializedName("icon")
    val iconUrl:String)
data class Main(
    @SerializedName("temp")
    val temperature:Double,
    @SerializedName("temp_min")
    val minTemperature:Double,
    @SerializedName("temp_max")
    val maxTemperature:Double,
)
data class Wind(
    @SerializedName("speed")
    val speed:Double,
    @SerializedName("deg")
    val degree:Double,
)

