package com.mahmoudelshahat.weatherandroidapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mahmoudelshahat.weatherandroidapp.databinding.ActivityMainBinding
import com.mahmoudelshahat.weatherandroidapp.location_manager.OnLocationReceivedListener
import com.mahmoudelshahat.weatherandroidapp.location_manager.WeatherLocationManager
import com.mahmoudelshahat.weatherandroidapp.ui.city_forecast.ForecastActivity
import com.mahmoudelshahat.weatherandroidapp.ui.current_weather.CurrentWeatherActivity
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.CITIES_KEY
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.LOCATION_CITY_KEY
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants.LOCATION_CODE
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherHelper
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherValidator
import kotlin.collections.ArrayList


class LandingActivity : AppCompatActivity(), OnLocationReceivedListener {
    private lateinit var binding: ActivityMainBinding

    //location items
    var locationManager: WeatherLocationManager? = null
    var mLocationCallback: LocationCallback? = null
    var fusedLocation: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewListeners()
    }

    private fun setupViewListeners() {
        binding.mainActivityButtonCitiesWeather.setOnClickListener {
            val citiesList = WeatherHelper
                .separateStringByComma(binding.mainActivityEditTextCities.text.toString())

            val response = WeatherValidator.validateCitiesList(citiesList)
            if (response.status) {
                val intent = Intent(this, CurrentWeatherActivity::class.java)
                intent.putStringArrayListExtra(CITIES_KEY, citiesList as ArrayList<String>)
                startActivity(intent)
            } else {
                showFeedback(response.message)
            }
        }

        binding.mainActivityButtonCurrentLocationWeather.setOnClickListener {
            setupLocationManager()
        }
    }


    private fun setupLocationManager() {
        //handling location
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val mLastLocation = locationResult.lastLocation
                val lng = mLastLocation.longitude
                val lat = mLastLocation.latitude
                getCountry(lat, lng)
            }
        }
        locationManager = WeatherLocationManager(
            fusedLocation!!, LOCATION_CODE,
            mLocationCallback!!, this, this
        )
        locationManager!!.getLastLocation()
    }

    private fun getCountry(lat: Double, lng: Double) {
        if (locationManager == null) return
        val country: String = locationManager?.getCountryName(this, lat, lng).toString()
        locationManager = null
        mLocationCallback = null

        val intent = Intent(this, ForecastActivity::class.java)
        intent.putExtra(LOCATION_CITY_KEY, country)
        startActivity(intent)
    }



    override fun onReceiveLocation(latitude: Double, longitude: Double) {
        getCountry(latitude, longitude)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationManager!!.getLastLocation()
        } else {
            showFeedback("Please enable permission in order to continue")
        }
    }
    private fun showFeedback(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {
        locationManager = null
        mLocationCallback = null
        super.onDestroy()
    }

}


