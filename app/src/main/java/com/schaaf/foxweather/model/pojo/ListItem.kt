package com.schaaf.foxweather.model.pojo

import com.google.gson.annotations.SerializedName
import com.schaaf.foxweather.model.ForecastRecyclerViewItem

data class ListItem (

    @field:SerializedName("dt")
    val dt: Int,

    @field:SerializedName("dt_txt")
    val dtTxt: String,

    @field:SerializedName("weather")
    val weather: List<WeatherItem?>?,

    @field:SerializedName("main")
    val main: Main?,

    @field:SerializedName("clouds")
    val clouds: Clouds?,

    @field:SerializedName("sys")
    val sys: Sys?,

    @field:SerializedName("wind")
    val wind: Wind?


) : ForecastRecyclerViewItem{
    override fun getItemType(): Int = 1

    override fun getDate(): Int = dt

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListItem

        if (dt != other.dt) return false
        if (dtTxt != other.dtTxt) return false
        if (weather != other.weather) return false
        if (main != other.main) return false
        if (clouds != other.clouds) return false
        if (sys != other.sys) return false
        if (wind != other.wind) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dt
        result = 31 * result + dtTxt.hashCode()
        result = 31 * result + (weather?.hashCode() ?: 0)
        result = 31 * result + (main?.hashCode() ?: 0)
        result = 31 * result + (clouds?.hashCode() ?: 0)
        result = 31 * result + (sys?.hashCode() ?: 0)
        result = 31 * result + (wind?.hashCode() ?: 0)
        return result
    }

}