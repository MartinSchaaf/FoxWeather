package com.schaaf.foxweather.model.city_search_paged_list

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.model.pojo.CityItem
import com.schaaf.foxweather.view_models.MainActivityViewModel

class CitySearchDataSourceFactory(
    val context: Context,
    private val viewModel: MainActivityViewModel,
    private val activity: MainActivity
) : DataSource.Factory<Int, CityItem>() {

    val sourceLiveData = MutableLiveData<CitySearchDataSource>()
    private lateinit var latestSource: CitySearchDataSource

    override fun create(): DataSource<Int, CityItem> {
        latestSource = CitySearchDataSource(context, viewModel, activity)
        sourceLiveData.postValue(latestSource)
        return latestSource
    }
}