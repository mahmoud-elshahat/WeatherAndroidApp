package com.mahmoudelshahat.weatherandroidapp.ui.current_weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoudelshahat.weatherandroidapp.data.model.CurrentWeather
import com.mahmoudelshahat.weatherandroidapp.data.model.CurrentWeatherFakeSample.sample
import com.mahmoudelshahat.weatherandroidapp.data.network.api.WeatherApiServiceBuilder
import com.mahmoudelshahat.weatherandroidapp.data.network.utils.NetworkStatus.*
import com.mahmoudelshahat.weatherandroidapp.data.repository.WeatherNetworkRepository
import com.mahmoudelshahat.weatherandroidapp.databinding.ActivityCurrentWeatherBinding
import com.mahmoudelshahat.weatherandroidapp.utils.EspressoIdlingResource
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.CITIES_KEY

class CurrentWeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCurrentWeatherBinding
    private var cities: ArrayList<String>? = null

    lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var adapter: CurrentWeatherAdapter

    private var weatherList: ArrayList<CurrentWeather> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.shimmerFrameLayout.startShimmerAnimation()

        getExtraDataFromIntent()
    }

    private fun getExtraDataFromIntent() {
        cities = intent.getStringArrayListExtra(CITIES_KEY)
        if (cities == null) {
            Toast.makeText(
                this, "An error occurred while opening screen", Toast.LENGTH_LONG
            ).show()
            onBackPressed()
            return
        }
        //for espresso test, wait till network call finish
        for(city in cities!!)
        EspressoIdlingResource.increase()

        setupViewModel()
        setupRecyclerView()
        fetchData()
    }

    private fun setupRecyclerView() {
        binding.currentWeatherRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = CurrentWeatherAdapter(arrayListOf())
        binding.currentWeatherRecyclerview.addItemDecoration(
            DividerItemDecoration(
                binding.currentWeatherRecyclerview.context,
                (binding.currentWeatherRecyclerview.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.currentWeatherRecyclerview.adapter = adapter
    }
    private fun setupViewModel() {
        val viewModelFactory = CurrentWeatherModelFactory(
            WeatherNetworkRepository(WeatherApiServiceBuilder.weatherApiService)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)
    }


    private fun fetchData() {
        for (city in cities!!) {
            viewModel.getCurrentWeather(city).observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> {
                            if (resource.data == null)
                                notifyAdapter(sample,city)
                            else
                                notifyAdapter(resource.data)
                        }
                        ERROR -> {
                            notifyAdapter(sample,city)
                        }
                        LOADING -> {

                        }
                    }
                }
            })
        }
    }

    private fun notifyAdapter(weather: CurrentWeather, city: String ="") {

        binding.shimmerFrameLayout.visibility = View.GONE
        binding.currentWeatherRecyclerview.visibility = View.VISIBLE
        if(city.isNotEmpty()){
            Toast.makeText(this,
                "Wrong city found '$city', it have been ignored",Toast.LENGTH_LONG).show()
            return
        }
        weatherList.add(weather)
        adapter.apply {
            addWeatherForCities(weatherList )
            notifyDataSetChanged()
        }
        EspressoIdlingResource.decrement()
    }

}