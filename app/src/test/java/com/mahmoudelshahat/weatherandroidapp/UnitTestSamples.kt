package com.mahmoudelshahat.weatherandroidapp

object UnitTestSamples {
    //String cities name
    // int expected number of returned cities
    fun getCommaSeparatedSamples(): HashMap<String, Int> {
        val commaSeparationSamples = HashMap<String, Int>()
        commaSeparationSamples["Egypt"] = 1
        commaSeparationSamples["Egypt,Canada"] = 2
        commaSeparationSamples["Egypt,UNITED STATES,ITALY"] = 3
        commaSeparationSamples["Egypt.ALEX.GERMANY"] = 1
        return commaSeparationSamples
    }


    fun getCitiesValidationSamples(): HashMap<String, Boolean> {
        val citiesTextSample = HashMap<String, Boolean>()

        citiesTextSample[""] = false
        citiesTextSample["Egypt,"] = false
        citiesTextSample["Egypt,  "] = false
        citiesTextSample["Egypt,Canada"] = false
        citiesTextSample["Egypt,Canada.Alex"] = false
        citiesTextSample["Egypt,Canada,Alex,Moscow,Russia,Japan,Germany,Berlin"] = false

        citiesTextSample["Egypt,Canada,Alex"] = true
        citiesTextSample["Egypt,Canada,Libya,Germany"] = true
        citiesTextSample["Egypt,Canada.Alex,Moscow,France,London"] = true
        citiesTextSample["Egypt,Canada.Alex,Moscow,France,London"] = true
        citiesTextSample["Egypt,Canada.Alex,Moscow,France,London,Russia"] = true
        return citiesTextSample
    }
}