package com.schaaf.foxweather.model.api_services

import com.schaaf.foxweather.BuildConfig.OWM_API_KEY
import com.schaaf.foxweather.model.pojo.CurrentWeatherPOJO
import com.schaaf.foxweather.model.pojo.ForecastPOJO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapRetrofitInterface {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") api_key: String = OWM_API_KEY,
        @Query("units") units: String = "metric"
    ): CurrentWeatherPOJO

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") api_key: String = OWM_API_KEY,
        @Query("units") units: String = "metric"
    ): ForecastPOJO
}

object OpenWeatherMapAPIService {

    val openWeatherMapAPIService: OpenWeatherMapRetrofitInterface by lazy {

        create()
    }

    private fun create(): OpenWeatherMapRetrofitInterface {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(OpenWeatherMapRetrofitInterface::class.java)
    }
}
