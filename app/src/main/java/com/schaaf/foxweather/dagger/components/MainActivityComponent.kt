package com.schaaf.foxweather.dagger.components

import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.dagger.PerActivity
import com.schaaf.foxweather.dagger.modules.ActivityModule
import com.schaaf.foxweather.dagger.modules.FavoriteCitiesFragmentModule
import com.schaaf.foxweather.dagger.modules.SettingsFragmentModule
import com.schaaf.foxweather.dagger.modules.WeatherInfoFragmentModule
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface MainActivityComponent {

    fun injectsMainActivity(activity: MainActivity)

    fun createWeatherInfoFragmentComponent(weatherInfoFragmentModule: WeatherInfoFragmentModule): WeatherInfoFragmentComponent

    fun createFavoriteCitiesFragmentComponent(favoriteCitiesFragmentModule: FavoriteCitiesFragmentModule):FavoriteCitiesFragmentComponent

    fun createSettingsFragmentComponent(settingsFragmentModule: SettingsFragmentModule): SettingsFragmentComponent

    @Subcomponent.Builder
    interface ActivityComponentBuilder {

        fun addActivityModule(activityModule: ActivityModule): ActivityComponentBuilder
        fun createComponent(): MainActivityComponent
    }
}