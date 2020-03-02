package com.schaaf.foxweather.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.schaaf.foxweather.app.App
import com.schaaf.foxweather.model.Constants.H_PA
import com.schaaf.foxweather.model.Constants.IMPERIAL
import com.schaaf.foxweather.model.Constants.METRIC
import com.schaaf.foxweather.model.Constants.MM_HG
import com.schaaf.foxweather.model.Constants.PRESSURE_MEASUREMENTS
import com.schaaf.foxweather.model.Constants.SAVED_CURRENT_WEATHER_DATA
import com.schaaf.foxweather.model.Constants.SAVED_FORECAST_DATA
import com.schaaf.foxweather.model.Constants.TEMPERATURE_MEASUREMENTS
import com.schaaf.foxweather.model.Constants.USER_CONFIG
import com.schaaf.foxweather.model.pojo.CurrentWeatherPOJO
import com.schaaf.foxweather.model.pojo.ForecastPOJO

object UserConfigSharedPreferences {

    private var sharedPreferences:SharedPreferences = App.appComponent.getContext().getSharedPreferences(USER_CONFIG, Context.MODE_PRIVATE)

    private val gson = Gson()



    //Saved state for last weather condition and forecast

    fun getSavedCurrentWeatherData():CurrentWeatherPOJO? {

        val stringJson = sharedPreferences.getString(SAVED_CURRENT_WEATHER_DATA,null)

        stringJson?.let {
            return gson.fromJson(it,CurrentWeatherPOJO::class.java)
        }

        return null
    }

    fun getSavedForecastData():ForecastPOJO?{

        val stringJson = sharedPreferences.getString(SAVED_FORECAST_DATA,null)

        stringJson?.let{
            return gson.fromJson(it,ForecastPOJO::class.java)
        }

        return null
    }

    fun saveForecastData(forecastPOJO: ForecastPOJO){

        val stringJson = gson.toJson(forecastPOJO)
        sharedPreferences.edit().putString(SAVED_FORECAST_DATA, stringJson).apply()
    }

    fun saveCurrentWeatherData(currentWeatherPOJO: CurrentWeatherPOJO){
        
        val stringJson = gson.toJson(currentWeatherPOJO)
        sharedPreferences.edit().putString(SAVED_CURRENT_WEATHER_DATA,stringJson).apply()
    }




    //Last uses measurements

    fun getPressureMeasurements():String{

        return sharedPreferences.getString(PRESSURE_MEASUREMENTS,MM_HG)!!
    }

    fun setPressureMeasurements(mType:String){

        if (mType == MM_HG || mType == H_PA)

        sharedPreferences.edit().putString(PRESSURE_MEASUREMENTS,mType).apply()

        else throw IllegalArgumentException(Throwable("Wrong measurements format, use hPa or mmHg"))
    }




    fun getTemperatureMeasurements():String{

        return sharedPreferences.getString(TEMPERATURE_MEASUREMENTS, METRIC)!!
    }

    fun setTemperatureMeasurements(measurementsType: String){

        if (measurementsType == METRIC || measurementsType == IMPERIAL)

            sharedPreferences.edit().putString(TEMPERATURE_MEASUREMENTS,measurementsType).apply()

        else throw IllegalArgumentException(Throwable("Wrong measurements format, use metric(for celsius) or imperial" +
                "(for fahrenheit)"))
    }

}