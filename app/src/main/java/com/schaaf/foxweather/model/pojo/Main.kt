package com.schaaf.foxweather.model.pojo

import com.google.gson.annotations.SerializedName

data class Main(

    @field:SerializedName("temp")
    val temp: Double,

    @field:SerializedName("temp_min")
    val tempMin: Double,

    @field:SerializedName("humidity")
    val humidity: Int,

    @field:SerializedName("pressure")
    var pressure: Double,

    @field:SerializedName("temp_max")
    val tempMax: Double
)