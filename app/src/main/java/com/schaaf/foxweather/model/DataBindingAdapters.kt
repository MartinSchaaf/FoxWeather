package com.schaaf.foxweather.model

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.schaaf.foxweather.R
import com.schaaf.foxweather.app.App
import com.schaaf.foxweather.model.Constants.ADMIN_NAME
import com.schaaf.foxweather.model.Constants.COUNTRY_NAME
import com.schaaf.foxweather.model.Constants.H_PA
import com.schaaf.foxweather.model.Constants.IMPERIAL
import com.schaaf.foxweather.model.Constants.METRIC
import com.schaaf.foxweather.model.Constants.MM_HG
import com.thbs.skycons.library.*
import org.decimal4j.util.DoubleRounder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object DataBindingAdapters {
    
    val context = App.appComponent.getContext()

    @JvmStatic
    @BindingAdapter("wind_degree")
    fun bindWindDegree(view:TextView, wind_degree:Double){

        view.text = when {
            (wind_degree in 0.0 .. 23.0) or (wind_degree in 338.0 .. 360.0) -> context.getString(R.string.wind_n)
            wind_degree in 24.0 .. 68.0 -> context.getString(R.string.wind_ne)
            wind_degree in 69.0 .. 113.0 -> context.getString(R.string.wind_e)
            wind_degree in 114.0 .. 158.0 -> context.getString(R.string.wind_se)
            wind_degree in 159.0 .. 203.0 -> context.getString(R.string.wind_s)
            wind_degree in 204.0 .. 248.0 -> context.getString(R.string.wind_sw)
            wind_degree in 249.0 .. 293.0 -> context.getString(R.string.wind_w)
            wind_degree in 294.0 .. 337.0 -> context.getString(R.string.wind_nw)

            else -> "-"
        }
    }

    @JvmStatic
    @BindingAdapter("weather_icon")
    fun bindWeatherIcon(layout: LinearLayout,weather_icon:String){

        layout.removeAllViews()

        val view =  when (weather_icon) {
            "01d" -> SunView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "02d" -> CloudSunView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "03d" -> CloudView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "04d" -> CloudView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "09d" -> CloudHvRainView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "10d" -> CloudRainView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "11d" -> CloudThunderView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "13d" -> CloudSnowView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "50d" -> CloudFogView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))

            "01n" -> MoonView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "02n" -> CloudMoonView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "03n" -> CloudView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "04n" -> CloudView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "09n" -> CloudHvRainView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "10n" -> CloudRainView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "11n" -> CloudThunderView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "13n" -> CloudSnowView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "50n" -> CloudFogView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))

            else -> CloudView(context,false,true,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
        }

        layout.addView(view)
    }

    @JvmStatic
    @BindingAdapter("forecast_weather_icon")
    fun bindForecastWeatherIcon(layout: LinearLayout,forecast_weather_icon:String){

        layout.removeAllViews()

        val view =  when (forecast_weather_icon) {
            "01d" -> SunView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "02d" -> CloudSunView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "03d" -> CloudView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "04d" -> CloudView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "09d" -> CloudHvRainView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "10d" -> CloudRainView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "11d" -> CloudThunderView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "13d" -> CloudSnowView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "50d" -> CloudFogView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))

            "01n" -> MoonView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "02n" -> CloudMoonView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "03n" -> CloudView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "04n" -> CloudView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "09n" -> CloudHvRainView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "10n" -> CloudRainView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "11n" -> CloudThunderView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "13n" -> CloudSnowView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
            "50n" -> CloudFogView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))

            else -> CloudView(context,true,false,ContextCompat.getColor(context,R.color.secondaryColor),ContextCompat.getColor(context,R.color.transparent))
        }

        layout.addView(view)
    }

    @JvmStatic
    @BindingAdapter("temperature")
    fun bindTemperature(view: TextView, temperature:Double){

        if (temperature != Constants.TEMP_NOT_AVAILABLE){

            val temp = temperature.roundToInt()

            fun bindTemp(stringResId:Int){
                view.text =  if (temp.toString().contains('-'))
                    context.getString(stringResId,"",temp)
                else
                    context.getString(stringResId,"+",temp)
            }

            if (UserConfigSharedPreferences.getTemperatureMeasurements() == IMPERIAL){

                bindTemp(R.string.temp_view_fahrenheit)
            }else{
                bindTemp(R.string.temp_view_celsius)
            }

        }else {
            view.text = context.getString(R.string.not_available)
        }
    }


    @JvmStatic
    @BindingAdapter("pressure")
    fun bindPressure(view: TextView, pressure:Double){

        if (pressure != Constants.PRESSURE_NOT_AVAILABLE){

            if(UserConfigSharedPreferences.getPressureMeasurements() == H_PA){

                view.text = context.getString(R.string.pressure_view_hPa,pressure.roundToInt())
            }else{

                val pressureInMmHg = pressure/1.333
                view.text = context.getString(R.string.pressure_view_mmHg,pressureInMmHg.roundToInt())
            }
        }else view.text = context.getString(R.string.not_available)
    }

    @JvmStatic
    @BindingAdapter("unix_date")
    fun bindDate(view:TextView, unix_date:Int){

        if (unix_date != Constants.DATE_NOT_AVAILABLE){

            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
            val date = Date(unix_date.toLong() * 1000)
            view.text = dateFormat.format(date)
        }else{
            view.text = context.getString(R.string.not_available)
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("forecast_date")
    fun bindForecastDate(view: TextView,forecast_date:Int){
        if (forecast_date != Constants.DATE_NOT_AVAILABLE){

            val dateFormat = SimpleDateFormat("dd MMMM yyyy, E", Locale.getDefault())
            val date = Date(forecast_date.toLong() * 1000)
            val text = dateFormat.format(date)
            view.text = "$text                                                                                            "
        }else{
            view.text = context.getString(R.string.not_available)
        }
    }

    @JvmStatic
    @BindingAdapter("time_from_date")
    fun bindTime(view: TextView,time_from_date:Int){
        if (time_from_date != Constants.DATE_NOT_AVAILABLE){

            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = Date(time_from_date.toLong() * 1000)
            view.text = dateFormat.format(date)
        }else{

            view.text = context.getString(R.string.not_available)
        }
    }

    @JvmStatic
    @BindingAdapter("windSpeed")
    fun bindWindSpeed(view: TextView,windSpeed:Double){

        var wSpeed = windSpeed

        if(UserConfigSharedPreferences.getTemperatureMeasurements() == IMPERIAL) wSpeed /= 2.237

        val roundedWindSpeed = DoubleRounder.round(wSpeed,1).toString()

        if(windSpeed != Constants.WIND_SPEED_NOT_AVAILABLE){
            view.text = context.getString(R.string.wind_speed_view,roundedWindSpeed)
        }else{

            view.text = context.getString(R.string.not_available)
        }
    }

    @JvmStatic
    @BindingAdapter("humidity")
    fun bindHumidity(view: TextView,humidity:Int){

        if (humidity != Constants.HUMIDITY_NOT_AVAILABLE){

            view.text = context.getString(R.string.humidity_view,humidity)
        }else{

            view.text = context.getString(R.string.not_available)
        }
    }

    @JvmStatic
    @BindingAdapter("is_favorite")
    fun setFavorite(view:CheckBox, is_favorite:Boolean){
        view.isChecked = is_favorite
    }

    @JvmStatic
    @BindingAdapter("uses_measurements")
    fun setMeasurements(view:RadioGroup,uses_measurements:String){

        if(uses_measurements == METRIC){
            view.findViewById<RadioButton>(R.id.celsius_radio_button).isChecked = true
        }

        if(uses_measurements == IMPERIAL){
            view.findViewById<RadioButton>(R.id.fahrenheit_radio_button).isChecked = true
        }

        if(uses_measurements == H_PA){
            view.findViewById<RadioButton>(R.id.hPa_radio_button).isChecked = true
        }

        if(uses_measurements == MM_HG){
            view.findViewById<RadioButton>(R.id.mmHg_radio_button).isChecked = true
        }
    }

    @JvmStatic
    @BindingAdapter("region")
    fun bindRegion(view: TextView, region: Bundle){

        val adminName = region.getString(ADMIN_NAME, "N/A")
        val countryName = region.getString(COUNTRY_NAME,"")

        val regionText = "$countryName $adminName"

        view.text = regionText
    }

}