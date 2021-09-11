package com.mahmoudelshahat.weatherandroidapp.ui.city_forecast

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.TextClassifier.TYPE_EMAIL
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mahmoudelshahat.weatherandroidapp.data.model.Forecast
import com.mahmoudelshahat.weatherandroidapp.databinding.CurrentWeatherListItemBinding
import com.mahmoudelshahat.weatherandroidapp.databinding.ForecastDayListItemBinding
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.IMAGE_BASE_URL
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.IMAGE_BIGGER_SIZE_EXTENSION
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.TYPE_HEADER
import java.util.*

class ForecastListAdapter(private val forecastList: ArrayList<Forecast>) :
    RecyclerView.Adapter<ViewHolder>() {

    inner class ItemViewHolder(val binding: CurrentWeatherListItemBinding) :
        ViewHolder(binding.root)

    inner class HeaderViewHolder(val binding: ForecastDayListItemBinding) : ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val binding = ForecastDayListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)

        } else {
            val binding = CurrentWeatherListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (forecastList[position].viewType == TYPE_HEADER) {
            with(holder as HeaderViewHolder) {
                with(forecastList[position]) {
                    with(binding) {
                        val x= headerTitle.split("-")
                        forecastDayText.text =x[2]+" / "+x[1]+" / "+x[0]
                    }
                }
            }
        }
        else {
            with(holder as ItemViewHolder) {
                with(forecastList[position]) {
                    with(binding) {
                        val timeEnd=endDateTime.split(" ")[1].split(":")
                        var timeStart =timeEnd[0].toInt()-3
                        if(timeStart<0)timeStart+=24

                        currentWeatherTextCity.text = weatherList[0].description

                        currentWeatherMinMaxTemp.text = "$timeStart:00 to ${timeEnd[0]}:${timeEnd[1]}"
                        timer.visibility=View.VISIBLE

                        currentWeatherTextTemp.text = mainObject.temperature.toString() + "°"
                        currentWeatherWindSpeed.text = wind.speed.toString()

                        Glide.with(currentWeatherImageIcon.context)
                            .load(IMAGE_BASE_URL + weatherList[0].iconUrl + IMAGE_BIGGER_SIZE_EXTENSION)
                            .into(currentWeatherImageIcon)
                    }

                }
            }
        }

    }


    override fun getItemCount(): Int = forecastList.size

    fun addForecastList(forecastList: List<Forecast>) {
        this.forecastList.apply {
            clear()
            addAll(forecastList)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return forecastList[position].viewType
    }
}