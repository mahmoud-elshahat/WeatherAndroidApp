package com.mahmoudelshahat.weatherandroidapp.location_manager

interface OnLocationReceivedListener {
    fun onReceiveLocation(latitude:Double,longitude:Double)
}