package com.schaaf.foxweather.view_models

import android.os.Bundle
import androidx.databinding.*
import androidx.lifecycle.ViewModel
import com.schaaf.foxweather.model.ForecastRecyclerViewItem
import com.schaaf.foxweather.model.Constants

class WeatherInfoFragmentViewModel : ViewModel() {

    //Data for current weather
    val icon: ObservableField<String?>? = ObservableField(Constants.ICON_NOT_AVAILABLE)

    val temp: ObservableDouble? = ObservableDouble(Constants.TEMP_NOT_AVAILABLE)

    val date: ObservableInt? = ObservableInt(Constants.DATE_NOT_AVAILABLE)

    val city: ObservableField<String?>? = ObservableField(Constants.CITY_NOT_AVAILABLE)

    val humidity: ObservableInt? = ObservableInt(Constants.HUMIDITY_NOT_AVAILABLE)

    val pressure: ObservableDouble? = ObservableDouble(Constants.PRESSURE_NOT_AVAILABLE)

    val windSpeed: ObservableDouble? = ObservableDouble(Constants.WIND_SPEED_NOT_AVAILABLE)

    val windDegree: ObservableDouble? = ObservableDouble(Constants.WIND_DEGREE_NOT_AVAILABLE)

    val region: ObservableField<Bundle>? = ObservableField(Bundle())

    //Data for forecast
    var forecastDataList: List<ForecastRecyclerViewItem>? = listOf()


}