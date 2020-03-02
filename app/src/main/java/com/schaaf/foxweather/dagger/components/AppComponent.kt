package com.schaaf.foxweather.dagger.components

import android.content.Context
import com.schaaf.foxweather.dagger.ComponentsHolder
import com.schaaf.foxweather.dagger.modules.AppModule
import com.schaaf.foxweather.model.room.DataBaseDao
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun injectsComponentsHolder(componentsHolder: ComponentsHolder)

    fun getContext():Context

    fun getDao():DataBaseDao

    @Component.Builder
    interface AppComponentBuilder{
        fun createComponent():AppComponent
        fun addAppModule(appModule: AppModule):AppComponentBuilder
    }
}