package com.schaaf.foxweather.dagger.modules

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.schaaf.foxweather.R
import com.schaaf.foxweather.databinding.FragmentFavoriteCitiesBinding
import com.schaaf.foxweather.fragments.FavoriteCitiesFragment
import com.schaaf.foxweather.model.favorite_cities_list.FavoriteCityRecyclerViewAdapter
import com.schaaf.foxweather.model.room.DataBaseDao
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.runBlocking

@Module
class FavoriteCitiesFragmentModule(
    val fragment: FavoriteCitiesFragment,
    private val inflater: LayoutInflater,
    private val container: ViewGroup?
) {

    @Provides
    fun provideDataBinding(): FragmentFavoriteCitiesBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_cities, container, false)

    @Provides
    fun provideFavoriteCityRecyclerViewAdapter(dao: DataBaseDao): FavoriteCityRecyclerViewAdapter =
        runBlocking {
            FavoriteCityRecyclerViewAdapter(
                dao.getAlltoList(),
                fragment
            )
        }
}