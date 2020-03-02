package com.schaaf.foxweather.dagger

import android.view.LayoutInflater
import android.view.ViewGroup
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.dagger.components.*
import com.schaaf.foxweather.dagger.modules.ActivityModule
import com.schaaf.foxweather.dagger.modules.FavoriteCitiesFragmentModule
import com.schaaf.foxweather.dagger.modules.SettingsFragmentModule
import com.schaaf.foxweather.dagger.modules.WeatherInfoFragmentModule
import com.schaaf.foxweather.fragments.FavoriteCitiesFragment
import com.schaaf.foxweather.fragments.SettingsFragment
import com.schaaf.foxweather.fragments.WeatherInfoFragment
import javax.inject.Inject
import javax.inject.Provider

class ComponentsHolder(appComponent: AppComponent) {

    init {
        appComponent.injectsComponentsHolder(this)
    }

    @Inject
    lateinit var mainActivityComponentBuilder: Provider<MainActivityComponent.ActivityComponentBuilder>
    private var mainActivityComponent: MainActivityComponent? = null

    private var weatherInfoFragmentComponent: WeatherInfoFragmentComponent? = null

    private var favoriteCitiesFragmentComponent: FavoriteCitiesFragmentComponent? = null

    private var settingsFragmentComponent: SettingsFragmentComponent? = null


    fun getMainActivityComponent(mainActivity: MainActivity): MainActivityComponent? {

        if (mainActivityComponent == null)
            mainActivityComponent =
                mainActivityComponentBuilder.get().addActivityModule(ActivityModule(mainActivity))
                    .createComponent()
        return mainActivityComponent
    }

    fun deleteMainActivityComponent() {
        mainActivityComponent = null
    }



    fun getWeatherInfoFragmentComponent(
        weatherInfoFragment: WeatherInfoFragment,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): WeatherInfoFragmentComponent? {

        if (weatherInfoFragmentComponent == null)
            weatherInfoFragmentComponent =
                mainActivityComponent?.createWeatherInfoFragmentComponent(
                    WeatherInfoFragmentModule(weatherInfoFragment, inflater, container)
                )
        return weatherInfoFragmentComponent
    }

    fun deleteWeatherInfoFragmentComponent() {
        weatherInfoFragmentComponent = null
    }



    fun getFavoriteCitiesFragmentComponent(
        favoriteCitiesFragment: FavoriteCitiesFragment,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FavoriteCitiesFragmentComponent? {
        if (favoriteCitiesFragmentComponent == null)
            favoriteCitiesFragmentComponent =
                mainActivityComponent?.createFavoriteCitiesFragmentComponent(
                    FavoriteCitiesFragmentModule(favoriteCitiesFragment, inflater, container)
                )
        return favoriteCitiesFragmentComponent
    }

    fun deleteFavoriteCitiesFragmentComponent() {
        favoriteCitiesFragmentComponent = null
    }



    fun getSettingsFragmentComponent(
        settingsFragment: SettingsFragment,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingsFragmentComponent? {
        if (settingsFragmentComponent == null)
            settingsFragmentComponent = mainActivityComponent?.createSettingsFragmentComponent(
                SettingsFragmentModule(settingsFragment, inflater, container)
            )
        return settingsFragmentComponent
    }

    fun deleteSettingsFragmentComponent() {
        settingsFragmentComponent = null
    }


}