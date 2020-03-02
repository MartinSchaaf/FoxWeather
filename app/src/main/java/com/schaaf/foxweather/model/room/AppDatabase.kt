package com.schaaf.foxweather.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteCity::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCityDao():DataBaseDao
}