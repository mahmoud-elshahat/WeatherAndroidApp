package com.mahmoudelshahat.weatherandroidapp

import com.mahmoudelshahat.weatherandroidapp.UnitTestSamples.getCitiesValidationSamples
import com.mahmoudelshahat.weatherandroidapp.UnitTestSamples.getCommaSeparatedSamples
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherHelper
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherValidator
import org.junit.Test

import org.junit.Assert.*

class UtilsUnitTest {
    @Test
    fun testSeparateByCommaDone() {
        val samplesMap=getCommaSeparatedSamples()
        for(key in samplesMap.keys){
            val list=WeatherHelper.separateStringByComma(key)
            assertEquals(samplesMap[key], list.size)
        }
    }


    @Test
    fun testCitiesTextValidation() {
        val validationSamples= getCitiesValidationSamples()
        for(key in validationSamples.keys){
            val response =WeatherValidator.validateCitiesList(WeatherHelper.separateStringByComma(key))
            assertEquals(response.status,validationSamples[key])
        }
    }
}