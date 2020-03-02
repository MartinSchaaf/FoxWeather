package com.schaaf.foxweather.dagger.modules

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import com.schaaf.foxweather.R
import com.schaaf.foxweather.dagger.*
import com.schaaf.foxweather.databinding.FragmentSettingsBinding
import com.schaaf.foxweather.fragments.SettingsFragment
import dagger.Module
import dagger.Provides

@Module
class SettingsFragmentModule(
    val fragment: SettingsFragment,
    private val inflater: LayoutInflater,
    private val container: ViewGroup?) {

    @Provides
    fun provideDataBinding(): FragmentSettingsBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

    @Provides
    @PerFragment
    @FahrenheitRadioButton
    fun provideFahrenheitRadioButton(binding: FragmentSettingsBinding):RadioButton =
        binding.root.findViewById(R.id.fahrenheit_radio_button)

    @Provides
    @PerFragment
    @CelsiusRadioButton
    fun provideCelsiusRadioButton(binding: FragmentSettingsBinding):RadioButton =
        binding.root.findViewById(R.id.celsius_radio_button)

    @Provides
    @PerFragment
    @mmHgRadioButton
    fun provideMmHgRadioButton(binding: FragmentSettingsBinding):RadioButton =
        binding.root.findViewById(R.id.mmHg_radio_button)

    @Provides
    @PerFragment
    @hPaRadioButton
    fun provideHPaRadioButton(binding: FragmentSettingsBinding):RadioButton =
        binding.root.findViewById(R.id.hPa_radio_button)

}