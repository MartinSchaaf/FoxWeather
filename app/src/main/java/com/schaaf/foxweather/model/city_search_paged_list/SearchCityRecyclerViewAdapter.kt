package com.schaaf.foxweather.model.city_search_paged_list

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.schaaf.foxweather.R
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.databinding.RecyclerItemRowSearchCityBinding
import com.schaaf.foxweather.model.Constants.ADMIN_NAME
import com.schaaf.foxweather.model.Constants.CITY_NAME
import com.schaaf.foxweather.model.Constants.COUNTRY_NAME
import com.schaaf.foxweather.model.Constants.FLAG_ON_ADD_CITY_TO_FAVORITE
import com.schaaf.foxweather.model.Constants.FLAG_ON_DELETE_CITY_FROM_FAVORITE
import com.schaaf.foxweather.model.Constants.H_PA
import com.schaaf.foxweather.model.Constants.IMPERIAL
import com.schaaf.foxweather.model.Constants.KEY_ADD_OR_DELETE_FAVORITE_CITY_FLAG
import com.schaaf.foxweather.model.Constants.LAT
import com.schaaf.foxweather.model.Constants.LNG
import com.schaaf.foxweather.model.Constants.METRIC
import com.schaaf.foxweather.model.Constants.MM_HG
import com.schaaf.foxweather.model.Constants.PRESSURE_UNITS_KEY
import com.schaaf.foxweather.model.Constants.UNITS_KEY
import com.schaaf.foxweather.model.RecyclerViewOnClickListener
import com.schaaf.foxweather.model.pojo.CityItem
import com.schaaf.foxweather.view_models.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class SearchCityRecyclerViewAdapter(

    private val viewModel: MainActivityViewModel,
    val activity:MainActivity,
    private val recyclerViewOnClick: RecyclerViewOnClickListener

) : PagedListAdapter<CityItem, SearchCityRecyclerViewAdapter.ViewHolder>(CitySearchDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RecyclerItemRowSearchCityBinding>(inflater, R.layout.recycler_item_row_search_city, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position)!!)

        if (position == itemCount - 1) viewModel.isProgressBarVisible.set(false)

        addTouchDelegateToFavoriteButton(holder)

        holder.binding.root.setOnClickListener {

            val cityItem = holder.binding.city!!

            val lat:String = cityItem.lat!!
            val lng:String = cityItem.lng!!
            val name = holder.binding.city!!.name
            val adminName1 = holder.binding.city!!.adminName1
            val countryName = holder.binding.city!!.countryName

            val bundle = Bundle()
            bundle.putDouble(LAT,lat.toDouble())
            bundle.putDouble(LNG,lng.toDouble())
            bundle.putString(CITY_NAME,name)
            bundle.putString(ADMIN_NAME, adminName1)
            bundle.putString(COUNTRY_NAME,countryName)

            setMeasurementsForApiRequest(bundle)

            recyclerViewOnClick.onClick(bundle)

            activity.search_view.closeSearch()

            if (activity.navController.currentDestination?.id == R.id.favoriteCitiesFragment)
                activity.navController.navigate(R.id.weatherInfoFragment)
        }


        holder.binding.favoriteButton.setOnClickListener {

            val cityItem = holder.binding.city!!

            val lat = cityItem.lat!!
            val lng = cityItem.lng!!
            val name = holder.binding.city!!.name
            val adminName1 = holder.binding.city!!.adminName1
            val countryName = holder.binding.city!!.countryName

            val bundle = Bundle()
            bundle.putDouble(LAT, lat.toDouble())
            bundle.putDouble(LNG, lng.toDouble())
            bundle.putString(CITY_NAME, name)
            bundle.putString(ADMIN_NAME, adminName1)
            bundle.putString(COUNTRY_NAME,countryName)

            setMeasurementsForApiRequest(bundle)

            val checkbox = holder.binding.favoriteButton

            if (checkbox.isChecked) {

                bundle.putString(KEY_ADD_OR_DELETE_FAVORITE_CITY_FLAG,FLAG_ON_ADD_CITY_TO_FAVORITE)
                recyclerViewOnClick.onClick(bundle)

            } else {

                bundle.putString(KEY_ADD_OR_DELETE_FAVORITE_CITY_FLAG,FLAG_ON_DELETE_CITY_FROM_FAVORITE)
                recyclerViewOnClick.onClick(bundle)
            }
            activity.search_view.closeSearch()
        }
    }

    private fun setMeasurementsForApiRequest(bundle: Bundle){

        if (activity.userConfigSharedPreferences.getTemperatureMeasurements() == IMPERIAL){
            bundle.putString(UNITS_KEY, IMPERIAL)
        }else{
            bundle.putString(UNITS_KEY, METRIC)
        }

        if (activity.userConfigSharedPreferences.getPressureMeasurements() == MM_HG){
            bundle.putString(PRESSURE_UNITS_KEY, MM_HG)
        }else{
            bundle.putString(PRESSURE_UNITS_KEY, H_PA)
        }
    }

    private fun addTouchDelegateToFavoriteButton(holder: ViewHolder){

        val parent = holder.binding.favoriteButton.parent as View

        parent.post {
            val rect = Rect()
            holder.binding.favoriteButton.getHitRect(rect)
            rect.top -= 20
            rect.left -= 20
            rect.bottom += 20
            rect.right += 20
            parent.touchDelegate = TouchDelegate(rect, holder.binding.favoriteButton)
        }
    }


    inner class ViewHolder(val binding: RecyclerItemRowSearchCityBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cityItem: CityItem) {
            binding.city = cityItem
        }
    }
}