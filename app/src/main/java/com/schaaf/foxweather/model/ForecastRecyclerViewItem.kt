package com.schaaf.foxweather.model

interface ForecastRecyclerViewItem {

    fun getItemType():Int
    fun getDate():Int
}