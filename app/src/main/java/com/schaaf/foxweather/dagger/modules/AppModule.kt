package com.schaaf.foxweather.dagger.modules

import android.content.Context
import androidx.room.Room
import com.schaaf.foxweather.dagger.components.MainActivityComponent
import com.schaaf.foxweather.model.room.AppDatabase
import com.schaaf.foxweather.model.room.DataBaseDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [MainActivityComponent::class])
class AppModule(val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideRoomDao(context: Context): DataBaseDao =
        Room.databaseBuilder(context, AppDatabase::class.java, "database")
            .build().getCityDao()

}