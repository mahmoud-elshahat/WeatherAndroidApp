package com.mahmoudelshahat.weatherandroidapp.data.model
import com.google.gson.annotations.SerializedName
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.TYPE_ITEM


class ForecastResponse (
    @SerializedName("list")
    val forecastList: List<Forecast>,
)

data class Forecast(
    val viewType:Int=TYPE_ITEM,
    @SerializedName("weather")
    val weatherList: List<Weather>,
    @SerializedName("main")
    val mainObject: Main,
    val wind: Wind,

    @SerializedName("dt_txt")
    val endDateTime: String,
    @SerializedName("dt")
    val startTimeStamp: String,

    val headerTitle:String=""
)