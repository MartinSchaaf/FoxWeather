package com.schaaf.foxweather.model.city_search_paged_list

import androidx.recyclerview.widget.DiffUtil
import com.schaaf.foxweather.model.pojo.CityItem

class CitySearchDiffUtil : DiffUtil.ItemCallback<CityItem>()  {

    override fun areItemsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {

        return oldItem.name == newItem.name && oldItem.countryName == newItem.countryName
    }

    override fun areContentsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {

        return oldItem.name == newItem.name && oldItem.countryName == newItem.countryName
    }


}