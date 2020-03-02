package com.schaaf.foxweather.model.pojo

import com.google.gson.annotations.SerializedName

data class CurrentWeatherPOJO(

    @SerializedName("dt")
	val dt: Int? = null,

    @SerializedName("coord")
	val coord: Coord?= null,

    @SerializedName("visibility")
	val visibility: Int?= null,

    @SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

    @field:SerializedName("name")
	val name: String? = null,

    @field:SerializedName("cod")
	val cod: Int? = null,

    @field:SerializedName("main")
	var main: Main? = null,

    @field:SerializedName("clouds")
	val clouds: Clouds? = null,

    @field:SerializedName("id")
	val id: Int? = null,

    @field:SerializedName("sys")
	val sys: Sys? = null,

    @field:SerializedName("base")
	val base: String? = null,

    @field:SerializedName("wind")
	val wind: Wind? = null,

    var nameFromGeoNames:String? = null,

    var adminName:String? = null,

    var countryName:String? = null
)
