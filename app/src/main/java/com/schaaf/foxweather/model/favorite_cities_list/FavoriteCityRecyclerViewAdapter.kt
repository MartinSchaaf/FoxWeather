package com.schaaf.foxweather.model.favorite_cities_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.schaaf.foxweather.R
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.databinding.RecyclerItemRowFavoriteCitiesBinding
import com.schaaf.foxweather.fragments.FavoriteCitiesFragment
import com.schaaf.foxweather.model.Constants.ADMIN_NAME
import com.schaaf.foxweather.model.Constants.CITY_NAME
import com.schaaf.foxweather.model.Constants.COUNTRY_NAME
import com.schaaf.foxweather.model.Constants.LAT
import com.schaaf.foxweather.model.Constants.LNG
import com.schaaf.foxweather.model.room.DataBaseDao
import com.schaaf.foxweather.model.room.FavoriteCity
import kotlinx.coroutines.*
import javax.inject.Inject

class FavoriteCityRecyclerViewAdapter(
    var data: List<FavoriteCity>?,
    val fragment: FavoriteCitiesFragment
) :
    RecyclerView.Adapter<FavoriteCityRecyclerViewAdapter.ViewHolder>() {

    @Inject lateinit var dao: DataBaseDao
    @Inject lateinit var activity: MainActivity

    init {
        fragment.daggerComponent.injectsFavoriteCitiesAdapter(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RecyclerItemRowFavoriteCitiesBinding>(
            inflater,
            R.layout.recycler_item_row_favorite_cities, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        data?.size?.let {

            holder.bind(data!![position])

            val binding = holder.binding

            binding.root.setOnClickListener {

                CoroutineScope(Dispatchers.IO).launch {

                    dao.getCity(binding.item!!.lat, binding.item!!.lng)
                        .let { city ->

                            val bundle = Bundle()

                            bundle.putDouble(LAT, city.lat)
                            bundle.putDouble(LNG, city.lng)
                            bundle.putString(CITY_NAME, city.name)
                            bundle.putString(ADMIN_NAME,city.adminName1)
                            bundle.putString(COUNTRY_NAME,city.countryName)

                            fragment.activityViewModel.currentCityDataForAPI.postValue(bundle)
                        }
                }

                activity.navController.navigate(R.id.weatherInfoFragment)
            }

            binding.root.setOnLongClickListener {

                val inflater = fragment.layoutInflater
                val view = inflater.inflate(R.layout.dialog_fragment_delete_favorite_city, null)

                val dialog = AlertDialog.Builder(activity)
                    .setView(view)
                    .setTitle(activity.getText(R.string.delete_city))
                    .create()

                val positiveButton = view.findViewById<Button>(R.id.yes_button)
                val negativeButton = view.findViewById<Button>(R.id.no_button)

                positiveButton.setOnClickListener {

                    CoroutineScope(Dispatchers.IO).launch {

                        dao.deleteCity(holder.binding.item!!)
                        dialog.dismiss()
                    }
                }
                negativeButton.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
                return@setOnLongClickListener true
            }
        }
    }


    inner class ViewHolder(val binding: RecyclerItemRowFavoriteCitiesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: FavoriteCity) {
            binding.item = city
        }
    }

    fun updateAdapter(newData: List<FavoriteCity>) {

        val diffUtilCallback =
            FavoriteCityDiffUtil(
                data!!,
                newData
            )
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }

}