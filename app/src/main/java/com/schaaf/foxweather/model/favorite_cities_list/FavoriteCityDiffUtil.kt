package com.schaaf.foxweather.model.favorite_cities_list

import androidx.recyclerview.widget.DiffUtil
import com.schaaf.foxweather.model.room.FavoriteCity

class FavoriteCityDiffUtil(
    private val oldList: List<FavoriteCity>,
    private val newList: List<FavoriteCity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}