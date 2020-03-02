package com.schaaf.foxweather.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.schaaf.foxweather.R
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.databinding.FragmentSettingsBinding
import com.schaaf.foxweather.view_models.MainActivityViewModel
import android.widget.RadioButton
import com.schaaf.foxweather.app.App
import com.schaaf.foxweather.dagger.CelsiusRadioButton
import com.schaaf.foxweather.dagger.FahrenheitRadioButton
import com.schaaf.foxweather.dagger.hPaRadioButton
import com.schaaf.foxweather.dagger.mmHgRadioButton
import com.schaaf.foxweather.model.Constants.H_PA
import com.schaaf.foxweather.model.Constants.IMPERIAL
import com.schaaf.foxweather.model.Constants.METRIC
import com.schaaf.foxweather.model.Constants.MM_HG
import com.schaaf.foxweather.model.UserConfigSharedPreferences
import javax.inject.Inject

class SettingsFragment : Fragment(),View.OnClickListener {

    @Inject lateinit var activityViewModel: MainActivityViewModel
    @Inject lateinit var binding: FragmentSettingsBinding
    @Inject lateinit var activity: MainActivity

    @Inject @CelsiusRadioButton
    lateinit var celsiusRadioButton: RadioButton
    @Inject @FahrenheitRadioButton
    lateinit var fahrenheitRadioButton: RadioButton
    @Inject @mmHgRadioButton
    lateinit var mmHgRadioButton: RadioButton
    @Inject @hPaRadioButton
    lateinit var hPaRadioButton: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        App.componentsHolder.getSettingsFragmentComponent(this,inflater,container)!!
            .injectsSettingsFragment(this)

        binding.fragment = this
        binding.sharedPreferences = UserConfigSharedPreferences

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        setBottomNavItemChecked()

        if (activityViewModel.isMenuVisible.get()) activity.hideMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving) App.componentsHolder.deleteSettingsFragmentComponent()
    }

    private fun setBottomNavItemChecked() {

        activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .menu
            .getItem(2)
            .isChecked = true
    }

    override fun onClick(view: View){

        when(view.id){

            R.id.celsius_radio_button -> {activity.userConfigSharedPreferences.setTemperatureMeasurements(METRIC)
                activity.refreshDataWithNewMeasurements(METRIC)
                activity.updateFavoriteCitiesData()
            }
            R.id.fahrenheit_radio_button -> {activity.userConfigSharedPreferences.setTemperatureMeasurements(IMPERIAL)
                activity.refreshDataWithNewMeasurements(IMPERIAL)
                activity.updateFavoriteCitiesData()
            }
            R.id.mmHg_radio_button -> {activity.userConfigSharedPreferences.setPressureMeasurements(MM_HG)
                activity.refreshDataWithNewMeasurements(MM_HG)
                activity.updateFavoriteCitiesData()
            }
            R.id.hPa_radio_button -> {activity.userConfigSharedPreferences.setPressureMeasurements(H_PA)
                activity.refreshDataWithNewMeasurements(H_PA)
                activity.updateFavoriteCitiesData()
            }
        }
    }
}
