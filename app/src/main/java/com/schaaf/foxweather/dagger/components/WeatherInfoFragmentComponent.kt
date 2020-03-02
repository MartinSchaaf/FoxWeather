package com.schaaf.foxweather.dagger.components

import com.schaaf.foxweather.dagger.modules.WeatherInfoFragmentModule
import com.schaaf.foxweather.fragments.WeatherInfoFragment
import dagger.Subcomponent

@Subcomponent(modules = [WeatherInfoFragmentModule::class])
interface WeatherInfoFragmentComponent {

    fun injectsFragment(weatherInfoFragment: WeatherInfoFragment)

}