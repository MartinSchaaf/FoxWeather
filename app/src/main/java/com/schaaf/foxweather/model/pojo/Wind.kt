package com.schaaf.foxweather.model.pojo

import com.google.gson.annotations.SerializedName

data class Wind(

    @field:SerializedName("deg")
    val deg: Double,

    @field:SerializedName("speed")
    val speed: Double
)