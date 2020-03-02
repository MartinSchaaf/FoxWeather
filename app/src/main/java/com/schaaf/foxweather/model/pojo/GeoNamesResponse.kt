package com.schaaf.foxweather.model.pojo

import com.google.gson.annotations.SerializedName

data class GeoNamesResponse(

	@field:SerializedName("totalResultsCount")
	val totalResultsCount: Int? = null,

	@field:SerializedName("geonames")
	val geonames: List<CityItem?>? = null
)