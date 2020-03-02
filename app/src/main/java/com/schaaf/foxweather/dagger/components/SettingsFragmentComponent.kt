package com.schaaf.foxweather.dagger.components

import com.schaaf.foxweather.dagger.PerFragment
import com.schaaf.foxweather.dagger.modules.SettingsFragmentModule
import com.schaaf.foxweather.fragments.SettingsFragment
import dagger.Subcomponent

@PerFragment
@Subcomponent(modules = [SettingsFragmentModule::class])
interface SettingsFragmentComponent {

    fun injectsSettingsFragment(settingsFragment: SettingsFragment)
}