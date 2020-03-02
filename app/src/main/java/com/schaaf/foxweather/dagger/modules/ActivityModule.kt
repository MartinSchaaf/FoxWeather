package com.schaaf.foxweather.dagger.modules

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.schaaf.foxweather.R
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.dagger.PerActivity
import com.schaaf.foxweather.databinding.ActivityMainBinding
import com.schaaf.foxweather.model.UserConfigSharedPreferences
import com.schaaf.foxweather.model.city_search_paged_list.CitySearchDataSourceFactory
import com.schaaf.foxweather.model.city_search_paged_list.SearchCityRecyclerViewAdapter
import com.schaaf.foxweather.view_models.MainActivityViewModel
import dagger.Module
import dagger.Provides


@Module
class ActivityModule(val activity: MainActivity) {

    @Provides
    @PerActivity
    fun provideMainActivity():MainActivity = activity

    @Provides
    @PerActivity
    fun provideViewModel(): MainActivityViewModel =
        ViewModelProviders.of(activity).get(MainActivityViewModel::class.java)

    @Provides
    @PerActivity
    fun provideDataBinding(): ActivityMainBinding =
        DataBindingUtil.setContentView(activity, R.layout.activity_main)

    @Provides
    @PerActivity
    fun provideRecyclerViewAdapter(viewModel: MainActivityViewModel): SearchCityRecyclerViewAdapter =
        SearchCityRecyclerViewAdapter(viewModel, activity, activity)

    @Provides
    @PerActivity
    fun provideUsersConfigSharedPreferences(): UserConfigSharedPreferences =
        UserConfigSharedPreferences

    @Provides
    @PerActivity
    fun provideCitiesDataSourceFactory(viewModel: MainActivityViewModel,activity: MainActivity): CitySearchDataSourceFactory =
        CitySearchDataSourceFactory(activity, viewModel,activity)

}

