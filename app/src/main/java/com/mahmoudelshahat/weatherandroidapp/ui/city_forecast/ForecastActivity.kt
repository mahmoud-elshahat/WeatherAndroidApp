package com.mahmoudelshahat.weatherandroidapp.ui.city_forecast

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoudelshahat.weatherandroidapp.data.model.CurrentWeatherFakeSample
import com.mahmoudelshahat.weatherandroidapp.data.model.Forecast
import com.mahmoudelshahat.weatherandroidapp.data.model.ForecastResponse
import com.mahmoudelshahat.weatherandroidapp.data.network.api.WeatherApiServiceBuilder
import com.mahmoudelshahat.weatherandroidapp.data.network.utils.NetworkStatus
import com.mahmoudelshahat.weatherandroidapp.data.repository.WeatherNetworkRepository
import com.mahmoudelshahat.weatherandroidapp.databinding.ActivityForecastBinding
import com.mahmoudelshahat.weatherandroidapp.utils.EspressoIdlingResource
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.TYPE_HEADER
import kotlin.collections.ArrayList


class ForecastActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForecastBinding

    private lateinit var viewModel: ForecastViewModel
    private lateinit var adapter: ForecastListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding view
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.shimmerFrameLayout.startShimmerAnimation()

        getCityFromIntentExtra()
    }


    private fun getCityFromIntentExtra() {
        val city = intent.getStringExtra(WeatherConstants.LOCATION_CITY_KEY)
        if (city == null) {
            Toast.makeText(
                this, "An error occurred while opening screen", Toast.LENGTH_LONG
            ).show()
            onBackPressed()
            return
        }
        //for espresso test, wait till network call finish
        EspressoIdlingResource.increase()

        supportActionBar?.title = city
        setupRecyclerView()
        setupViewModel()
        fetchData(city)
    }


    private fun setupRecyclerView() {
        binding.forecastRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = ForecastListAdapter(arrayListOf())
        binding.forecastRecyclerview.addItemDecoration(
            DividerItemDecoration(
                binding.forecastRecyclerview.context,
                (binding.forecastRecyclerview.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.forecastRecyclerview.adapter = adapter
    }
    private fun setupViewModel() {
        val viewModelFactory = ForecastModelFactory(
            WeatherNetworkRepository(WeatherApiServiceBuilder.weatherApiService)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ForecastViewModel::class.java)
    }


    private fun fetchData(city: String) {
            viewModel.getForecastFiveDays(city).observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        NetworkStatus.SUCCESS -> {
                            if (resource.data == null)
                                Toast.makeText(
                                    this,
                                    "Error has been occurred", Toast.LENGTH_LONG
                                ).show()
                            else
                                notifyAdapter(resource.data)
                        }
                        NetworkStatus.ERROR -> {
                            Toast.makeText(
                                this,
                                "Error has been occurred", Toast.LENGTH_LONG
                            ).show()
                        }
                        NetworkStatus.LOADING -> {
                        }
                    }
                }
            })
    }

    private fun notifyAdapter(forecastResponse: ForecastResponse) {
        Log.e("TAG", "notifyAdapter: 1")
        val forecastListWithHeaders = ArrayList<Forecast>()
        forecastListWithHeaders.add(
            Forecast(
                TYPE_HEADER,
                listOf(),
                CurrentWeatherFakeSample.sample.mainObject,
                CurrentWeatherFakeSample.sample.wind,
                "",
                "",
                headerTitle = forecastResponse.forecastList[0].endDateTime.split(" ")[0]
            )
        )


        Log.e("TAG", "notifyAdapter: 2")

        for((count, forecast) in forecastResponse.forecastList.withIndex()){
            Log.e("TAG", "notifyAdapter: in ")
            Log.e("TAG", "notifyAdapter: count = $count")

            forecastListWithHeaders.add(forecast)
            if(getHoursFromDateTime(forecast.endDateTime) == "00"){
                if(count+1 < forecastResponse.forecastList.size)
                forecastListWithHeaders.add(
                    Forecast(
                        TYPE_HEADER,
                        listOf(),
                        CurrentWeatherFakeSample.sample.mainObject,
                        CurrentWeatherFakeSample.sample.wind,
                        "",
                        "",
                        headerTitle = forecastResponse.forecastList[count].endDateTime.split(" ")[0]
                    )
                )
            }
        }
        Log.e("TAG", "notifyAdapter: 3")
        Log.e("TAG", "notifyAdapter: LIST SIZE= " + forecastListWithHeaders.size)

        EspressoIdlingResource.decrement()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.forecastRecyclerview.visibility = View.VISIBLE
        adapter.apply {
            addForecastList(forecastListWithHeaders)
            notifyDataSetChanged()
        }
    }


    private fun getHoursFromDateTime(dateTime: String):String{
        val items=dateTime.split(" ")[1].split(":")
        return items[0]
    }

}