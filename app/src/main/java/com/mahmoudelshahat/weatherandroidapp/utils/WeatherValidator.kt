package com.mahmoudelshahat.weatherandroidapp.utils

object WeatherValidator {
    fun validateCitiesList(cities:List<String>):ValidationResponse{
        var message=""
        when {
            cities.size<3 -> {
                message="Min cities can be entered is 3"
            }
            cities.size>7 -> {
                message="Max cities can be entered is 7"
            }
            else -> {
                for( city in cities){
                    if(city.trim().isEmpty()){
                        message="City cant be empty"
                    }
                }
            }
        }
        return ValidationResponse(message.isEmpty(),message)
    }
}
class ValidationResponse(val status:Boolean ,val message:String)