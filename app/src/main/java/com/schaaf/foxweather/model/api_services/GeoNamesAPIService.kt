package com.schaaf.foxweather.model.api_services

import com.schaaf.foxweather.BuildConfig.GEONAMES_API_KEY
import com.schaaf.foxweather.model.pojo.GeoNamesResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface GeoNamesRetrofitInterface {

    @GET("/searchJSON?")
    suspend fun getCities(
        @Query("name_startsWith") givenCharacters: String,
        @Query("maxRows") listSize: Int,
        @Query("startRow") startRow: Int,
        @Query("username") username: String = GEONAMES_API_KEY,
        @Query("lang") lang: String = when (Locale.getDefault().toLanguageTag()) {
            "en-US" -> "en"
            "ru-RU" -> "ru"
            else -> "en"
        },
        @Query("orderby") orderBy: String = "population",
        @Query("featureClass") fClass: String = "P"
    ): GeoNamesResponse
}

object GeoNamesAPIService {

    val geoNamesService: GeoNamesRetrofitInterface by lazy {

        createGeoNamesService()
    }

    private const val BASE_URL = "http://api.geonames.org"

    private fun createGeoNamesService(): GeoNamesRetrofitInterface {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(GeoNamesRetrofitInterface::class.java)

    }

}