package com.schaaf.foxweather.model.city_search_paged_list

import android.content.Context
import android.widget.Toast
import androidx.paging.PositionalDataSource
import com.schaaf.foxweather.R
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.app.App
import com.schaaf.foxweather.model.api_services.GeoNamesAPIService
import com.schaaf.foxweather.model.pojo.CityItem
import com.schaaf.foxweather.model.pojo.GeoNamesResponse
import com.schaaf.foxweather.model.room.DataBaseDao
import com.schaaf.foxweather.view_models.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class CitySearchDataSource(
    private val ctx: Context,
    var viewModel: MainActivityViewModel,
    val activity: MainActivity
) : PositionalDataSource<CityItem>() {

    var dao: DataBaseDao = App.appComponent.getDao()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CityItem>) {

        CoroutineScope(IO).launch {

            try {

                val response = GeoNamesAPIService.geoNamesService.getCities(
                    viewModel.searchViewInput.value?.trim()!!,
                    params.loadSize,
                    params.startPosition
                )
                setFavoriteCheckboxCheckedIfResponseContainsExistsFavoriteCities(response)
                callback.onResult(response.geonames!!)

            } catch (e: Throwable) {

                e.printStackTrace()
                withContext(Main) {

                    Toast.makeText(ctx, ctx.getText(R.string.ic_error), Toast.LENGTH_SHORT).show()
                    activity.search_view.closeSearch()
                }
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<CityItem>
    ) {
        viewModel.searchViewInput.value?.let { textInput ->

            CoroutineScope(IO).launch {

                try {
                    val response = GeoNamesAPIService.geoNamesService.getCities(
                        textInput.trim(),
                        params.requestedLoadSize,
                        0
                    )

                    if(response.totalResultsCount == 0){
                        activity.viewModel.isCityNotFoundLayoutVisible.set(true)
                        activity.viewModel.isProgressBarVisible.set(false)
                    }else
                        setFavoriteCheckboxCheckedIfResponseContainsExistsFavoriteCities(response)

                    callback.onResult(response.geonames!!, 0)

                } catch (e: Throwable) {

                    e.printStackTrace()
                    withContext(Main){

                        Toast.makeText(ctx, ctx.getText(R.string.ic_error), Toast.LENGTH_SHORT).show()
                        activity.search_view.closeSearch()
                    }
                }
            }
        }
    }

    private suspend fun setFavoriteCheckboxCheckedIfResponseContainsExistsFavoriteCities(response: GeoNamesResponse) {

        val favoriteCitiesList = dao.getAlltoList()

        response.geonames?.forEach { cityItem ->

            favoriteCitiesList.forEach {

                if (cityItem?.name == it.name && cityItem.adminName1 == it.adminName1) {
                    cityItem.isFavorite = true
                }
            }
        }
    }


}