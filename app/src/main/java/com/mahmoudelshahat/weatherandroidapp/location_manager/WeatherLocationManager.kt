@file:Suppress("DEPRECATION")

package com.mahmoudelshahat.weatherandroidapp.location_manager


import android.Manifest
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.mahmoudelshahat.weatherandroidapp.utils.PermissionUtils
import java.io.IOException
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION ,MissingPermission")
class WeatherLocationManager(private var fusedLocation: FusedLocationProviderClient,
                       private val LOCATION_CODE: Int,
                       private val mLocationCallback: LocationCallback,
                       private val onLocationReceivedListener: OnLocationReceivedListener,
                       val context: AppCompatActivity) {


    //ask for location permission
    private fun askForLocationPermission() {
        !PermissionUtils.requestGrantedPermissions(context,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_CODE)
    }
    //check if permission already gotten
    private fun checkPermissions(): Boolean {
        return (PermissionUtils.isPermissionsGranted(context,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)))
    }
    // check if location of device is enabled or not
    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocation.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        onLocationReceivedListener.onReceiveLocation(location.latitude, location.longitude)
                    }
                }
            } else {
                displayLocationSettingsRequest()
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            askForLocationPermission()
        }
    }

    // dispaly a dailog to open user location
    private fun displayLocationSettingsRequest() {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()


        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5
        locationRequest.fastestInterval = (1000 / 2).toLong()


        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { res ->
            val status: Status = res.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS ->
                    requestNewLocationData()
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        status.startResolutionForResult(context, LOCATION_CODE)
                        requestNewLocationData()

                    } catch (e: SendIntentException) {
                    }
                }
                LocationSettingsStatusCodes.CANCELED ->{
                }
            }
        }
    }


    //set a listener for location updates
    private fun requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 10

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocation = LocationServices.getFusedLocationProviderClient(context)
        fusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    fun getCountryName(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            return if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].adminArea
            } else null
        } catch (ignored: IOException) {
            //do something
        }
        return null
    }

}