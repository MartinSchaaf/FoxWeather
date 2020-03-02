package com.schaaf.foxweather.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataBaseDao   {

    @Query("SELECT * FROM Favorite_Cities")
    fun getAlltoLiveData() : LiveData<List<FavoriteCity>>

    @Query("SELECT * FROM Favorite_Cities")
    suspend fun getAlltoList():List<FavoriteCity>

    @Query("SELECT * FROM Favorite_Cities WHERE lat = :lat AND lng = :lng")
    suspend fun getCity(lat:Double, lng:Double):FavoriteCity

    @Insert
    suspend fun addCity(city: FavoriteCity)

    @Query("DELETE FROM Favorite_Cities WHERE name = :name AND adminName1 = :adminName1")
    suspend fun deleteCity(name: String,adminName1: String)

    @Delete
    suspend fun deleteCity(city: FavoriteCity)

    @Query("DELETE FROM FAVORITE_CITIES")
    suspend fun deleteAll()

    @Query("UPDATE Favorite_Cities SET pressure = :newPressure, `temp` = :newTemp WHERE name = :name AND 'adminName1' = :adminName")
    suspend fun updateTempAndPressure(newPressure:Double, newTemp:Double, name:String, adminName: String)
}