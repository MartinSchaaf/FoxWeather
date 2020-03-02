package com.schaaf.foxweather.view_models

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val searchViewInput: MutableLiveData<String> = MutableLiveData()

    val currentCityDataForAPI: MutableLiveData<Bundle> =
        MutableLiveData() //Includes lat,lng and city name

    val isProgressBarVisible: ObservableBoolean = ObservableBoolean()

    val isCityNotFoundLayoutVisible: ObservableBoolean =
        ObservableBoolean() //Visibility for layout, that includes "city not fond" string and icon

    val isRecyclerViewVisible: ObservableBoolean = ObservableBoolean()

    val isMenuVisible: ObservableBoolean = ObservableBoolean()

    val isLoadAndBindProgressBarVisible:ObservableBoolean = ObservableBoolean()
}