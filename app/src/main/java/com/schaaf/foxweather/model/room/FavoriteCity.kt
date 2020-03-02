package com.schaaf.foxweather.model.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Favorite_Cities")
data class FavoriteCity(

    var name: String,
    var date: Int,
    var temp: Double,
    var icon: String,
    var humidity: Int,
    var windSpeed: Double,
    var windDegree: Double,
    var pressure: Double,
    var adminName1: String,
    var lat: Double,
    var lng: Double,
    var countryName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}