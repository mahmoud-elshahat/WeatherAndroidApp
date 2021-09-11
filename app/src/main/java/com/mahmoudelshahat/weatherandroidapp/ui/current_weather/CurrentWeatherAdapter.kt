package com.mahmoudelshahat.weatherandroidapp.ui.current_weather
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mahmoudelshahat.weatherandroidapp.data.model.CurrentWeather
import com.mahmoudelshahat.weatherandroidapp.databinding.CurrentWeatherListItemBinding
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.IMAGE_BASE_URL
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.IMAGE_BIGGER_SIZE_EXTENSION

class CurrentWeatherAdapter(private val currentWeatherList: ArrayList<CurrentWeather>)
    : RecyclerView.Adapter<CurrentWeatherAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: CurrentWeatherListItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CurrentWeatherListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(currentWeatherList[position]){
                with(binding){
                    currentWeatherTextCity.text=city+" - "+weatherList[0].description
                    currentWeatherMinMaxTemp.text="${mainObject.minTemperature}° - ${mainObject.maxTemperature}°"
                    currentWeatherTextTemp.text=mainObject.temperature.toString()+"°"
                    currentWeatherWindSpeed.text=wind.speed.toString()

                    Glide.with(currentWeatherImageIcon.context)
                        .load(IMAGE_BASE_URL+weatherList[0].iconUrl+ IMAGE_BIGGER_SIZE_EXTENSION)
                        .into(currentWeatherImageIcon)
                }

            }
        }
    }


    override fun getItemCount(): Int = currentWeatherList.size

    fun addWeatherForCities(weatherList: List<CurrentWeather>) {
        this.currentWeatherList.apply {
            clear()
            addAll(weatherList)
        }

    }

}