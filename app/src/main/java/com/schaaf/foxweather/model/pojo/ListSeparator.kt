package com.schaaf.foxweather.model.pojo

import com.schaaf.foxweather.model.ForecastRecyclerViewItem

data class ListSeparator(var date_:Int
) : ForecastRecyclerViewItem {
    override fun getItemType(): Int =0

    override fun getDate(): Int = date_

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListSeparator

        if (date_ != other.date_) return false

        return true
    }

    override fun hashCode(): Int {
        return date_
    }
}