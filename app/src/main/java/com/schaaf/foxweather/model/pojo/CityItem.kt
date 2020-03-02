package com.schaaf.foxweather.model.pojo

import com.google.gson.annotations.SerializedName

data class CityItem(

    @field:SerializedName("adminCode1")
    val adminCode1: String? = null,

    @field:SerializedName("lng")
    val lng: String? = null,

    @field:SerializedName("geonameId")
    val geonameId: Int? = null,

    @field:SerializedName("toponymName")
    val toponymName: String? = null,

    @field:SerializedName("countryId")
    val countryId: String? = null,

    @field:SerializedName("fcl")
    val fcl: String? = null,

    @field:SerializedName("population")
    val population: Int? = null,

    @field:SerializedName("countryCode")
    val countryCode: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("fclName")
    val fclName: String? = null,

    @field:SerializedName("adminCodes1")
    val adminCodes1: AdminCodes1? = null,

    @field:SerializedName("countryName")
    val countryName: String? = null,

    @field:SerializedName("fcodeName")
    val fcodeName: String? = null,

    @field:SerializedName("adminName1")
    val adminName1: String? = null,

    @field:SerializedName("lat")
    val lat: String? = null,

    @field:SerializedName("fcode")
    val fcode: String? = null,

    var isFavorite:Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CityItem

        if (name != other.name) return false
        if (countryName != other.countryName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (countryName?.hashCode() ?: 0)
        return result
    }
}