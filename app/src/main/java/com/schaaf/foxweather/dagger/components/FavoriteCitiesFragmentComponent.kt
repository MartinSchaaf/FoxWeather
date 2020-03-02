package com.schaaf.foxweather.dagger.components

import com.schaaf.foxweather.dagger.modules.FavoriteCitiesFragmentModule
import com.schaaf.foxweather.fragments.FavoriteCitiesFragment
import com.schaaf.foxweather.model.favorite_cities_list.FavoriteCityRecyclerViewAdapter
import dagger.Subcomponent

@Subcomponent(modules = [FavoriteCitiesFragmentModule::class])
interface FavoriteCitiesFragmentComponent {

    fun injectsFavoriteCitiesFragment(favoriteCitiesFragment: FavoriteCitiesFragment)

    fun injectsFavoriteCitiesAdapter(favoriteCitiesRecyclerViewAdapter: FavoriteCityRecyclerViewAdapter)
}