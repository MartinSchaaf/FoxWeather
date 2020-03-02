package com.schaaf.foxweather.app

import android.app.Application
import com.schaaf.foxweather.dagger.ComponentsHolder
import com.schaaf.foxweather.dagger.components.AppComponent
import com.schaaf.foxweather.dagger.components.DaggerAppComponent
import com.schaaf.foxweather.dagger.modules.AppModule

class App : Application() {

    companion object{

        lateinit var appComponent:AppComponent
        lateinit var componentsHolder: ComponentsHolder
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().addAppModule(AppModule(this.applicationContext)).createComponent()
        componentsHolder = ComponentsHolder(appComponent)

    }

}