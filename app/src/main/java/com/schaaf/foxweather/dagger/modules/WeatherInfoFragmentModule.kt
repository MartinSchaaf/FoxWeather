package com.schaaf.foxweather.dagger.modules

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.schaaf.foxweather.R
import com.schaaf.foxweather.databinding.FragmentWeatherInfoBinding
import com.schaaf.foxweather.fragments.WeatherInfoFragment
import com.schaaf.foxweather.view_models.WeatherInfoFragmentViewModel
import dagger.Module
import dagger.Provides

@Module
class WeatherInfoFragmentModule(
    val fragment: WeatherInfoFragment,
    private val inflater: LayoutInflater,
    private val container: ViewGroup?) {

    @Provides
    fun provideViewModel(): WeatherInfoFragmentViewModel =
        ViewModelProviders.of(fragment).get(WeatherInfoFragmentViewModel::class.java)

    @Provides
    fun provideDataBinding(): FragmentWeatherInfoBinding =
        DataBindingUtil.inflate(inflater,R.layout.fragment_weather_info, container, false)
}