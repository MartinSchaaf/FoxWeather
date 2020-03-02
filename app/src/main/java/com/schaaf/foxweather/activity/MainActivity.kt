package com.schaaf.foxweather.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.schaaf.foxweather.R
import com.schaaf.foxweather.app.App
import com.schaaf.foxweather.databinding.ActivityMainBinding
import com.schaaf.foxweather.model.Constants.ADMIN_NAME
import com.schaaf.foxweather.model.Constants.CITY_NAME
import com.schaaf.foxweather.model.Constants.COUNTRY_NAME
import com.schaaf.foxweather.model.Constants.FLAG_ON_ADD_CITY_TO_FAVORITE
import com.schaaf.foxweather.model.Constants.H_PA
import com.schaaf.foxweather.model.Constants.IMPERIAL
import com.schaaf.foxweather.model.Constants.KEY_ADD_OR_DELETE_FAVORITE_CITY_FLAG
import com.schaaf.foxweather.model.Constants.LAT
import com.schaaf.foxweather.model.Constants.LNG
import com.schaaf.foxweather.model.Constants.METRIC
import com.schaaf.foxweather.model.Constants.MM_HG
import com.schaaf.foxweather.model.Constants.PRESSURE_UNITS_KEY
import com.schaaf.foxweather.model.Constants.TEMPERATURE_UNITS_KEY
import com.schaaf.foxweather.model.RecyclerViewOnClickListener
import com.schaaf.foxweather.model.UserConfigSharedPreferences
import com.schaaf.foxweather.model.api_services.OpenWeatherMapAPIService
import com.schaaf.foxweather.model.city_search_paged_list.CitySearchDataSourceFactory
import com.schaaf.foxweather.model.city_search_paged_list.SearchCityRecyclerViewAdapter
import com.schaaf.foxweather.model.pojo.CityItem
import com.schaaf.foxweather.model.room.DataBaseDao
import com.schaaf.foxweather.model.room.FavoriteCity
import com.schaaf.foxweather.view_models.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.inject.Inject


class MainActivity : AppCompatActivity(), RecyclerViewOnClickListener {

    @Inject lateinit var viewModel: MainActivityViewModel
    @Inject lateinit var searchCityAdapter: SearchCityRecyclerViewAdapter
    @Inject lateinit var binding: ActivityMainBinding
    @Inject lateinit var userConfigSharedPreferences: UserConfigSharedPreferences
    @Inject lateinit var citySearchDataSourceFactory: CitySearchDataSourceFactory
    @Inject lateinit var dao: DataBaseDao

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.componentsHolder.getMainActivityComponent(this)?.injectsMainActivity(this)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
    }

    override fun onRestart() {
        super.onRestart()

        App.componentsHolder.getMainActivityComponent(this)?.injectsMainActivity(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu, menu)

        val menuItem: MenuItem = menu!!.findItem(R.id.action_search)
        search_view.setMenuItem(menuItem)

        if (viewModel.isMenuVisible.get()) {
            menuItem.isVisible = true
        } else {
            menuItem.isVisible = false
            search_view.closeSearch()
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()

        binding.vmodel = viewModel

        search_view.setHint(this.getString(R.string.et_hint))

        searchCityRecyclerView.layoutManager = LinearLayoutManager(this)
        searchCityRecyclerView.adapter = searchCityAdapter

        setSearchViewOnQueryTextListener()
        setAnimationOnSearchViewListener()

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        updateFavoriteCitiesData()
    }


    override fun onResume() {
        super.onResume()

        setSearchViewPagedList()

        viewModel.searchViewInput.observe(this, Observer { userTextInput ->

            if (userTextInput.isNotEmpty() && userTextInput.isNotBlank()) {

                if (!viewModel.isRecyclerViewVisible.get()) viewModel.isRecyclerViewVisible.set(true)

                if (viewModel.isCityNotFoundLayoutVisible.get())
                    viewModel.isCityNotFoundLayoutVisible.set(false)

                viewModel.isProgressBarVisible.set(true)
                citySearchDataSourceFactory.sourceLiveData.value?.invalidate()
            }

            if (userTextInput.isBlank() || userTextInput.isEmpty()) {
                viewModel.isProgressBarVisible.set(false)
                viewModel.isRecyclerViewVisible.set(false)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            App.componentsHolder.deleteMainActivityComponent()
        }
    }

    override fun onClick(bundle: Bundle) {

        if (bundle.getString(KEY_ADD_OR_DELETE_FAVORITE_CITY_FLAG) != null) {

            runBlocking {

                val flag = bundle.getString(KEY_ADD_OR_DELETE_FAVORITE_CITY_FLAG)
                val name = bundle.get(CITY_NAME) as String
                val adminName1 = bundle.get(ADMIN_NAME) as String
                val countryName = bundle.getString(COUNTRY_NAME)

                if (flag == FLAG_ON_ADD_CITY_TO_FAVORITE) {

                    val lng = bundle.getDouble(LNG)
                    val lat = bundle.getDouble(LAT)

                    try {

                        val currentWeatherResponse =
                            OpenWeatherMapAPIService.openWeatherMapAPIService.getCurrentWeather(
                                lat,
                                lng,
                                units = UserConfigSharedPreferences.getTemperatureMeasurements()
                            )

                        val city = FavoriteCity(
                            name,
                            currentWeatherResponse.dt!!,
                            currentWeatherResponse.main!!.temp,
                            currentWeatherResponse.weather!![0]!!.icon!!,
                            currentWeatherResponse.main!!.humidity,
                            currentWeatherResponse.wind!!.speed,
                            currentWeatherResponse.wind.deg,
                            currentWeatherResponse.main!!.pressure,
                            adminName1,
                            lat,
                            lng,
                            countryName!!
                        )

                        if (!dao.getAlltoList().contains(city)) dao.addCity(city)

                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }

                } else dao.deleteCity(name, adminName1)
            }

        } else viewModel.currentCityDataForAPI.value = bundle
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    override fun onBackPressed() {

        if (search_view.isSearchOpen) search_view.closeSearch()

        if (viewModel.isCityNotFoundLayoutVisible.get())
            viewModel.isCityNotFoundLayoutVisible.set(false)

        else super.onBackPressed()
    }


    private fun setSearchViewOnQueryTextListener() {

        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {

                viewModel.searchViewInput.postValue(newText)
                return false
            }
        })
    }


    private fun setAnimationOnSearchViewListener() {

        fun backgroundBlackout(toAlpha: Float) {

            black_background.animate().apply {
                interpolator = LinearInterpolator()
                duration = 350
                alpha(toAlpha)
                start()
            }
        }

        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {

            override fun onSearchViewClosed() = backgroundBlackout(0.0f)

            override fun onSearchViewShown() = backgroundBlackout(0.5f)
        })

    }

    private fun setSearchViewPagedList() {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(15)
            .build()

        val citiesListLiveData: LiveData<PagedList<CityItem>> =
            LivePagedListBuilder(citySearchDataSourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build()

        citiesListLiveData.observe(this@MainActivity,
            Observer<PagedList<CityItem>> { t -> searchCityAdapter.submitList(t) })
    }

    fun refreshDataWithNewMeasurements(newMeasurements: String) {

        when (newMeasurements) {
            METRIC -> viewModel.currentCityDataForAPI.value?.putString(
                TEMPERATURE_UNITS_KEY,
                METRIC
            )
            IMPERIAL -> viewModel.currentCityDataForAPI.value?.putString(
                TEMPERATURE_UNITS_KEY,
                IMPERIAL
            )
            MM_HG -> viewModel.currentCityDataForAPI.value?.putString(PRESSURE_UNITS_KEY, MM_HG)
            H_PA -> viewModel.currentCityDataForAPI.value?.putString(PRESSURE_UNITS_KEY, H_PA)
        }
    }

    fun updateFavoriteCitiesData() {

        CoroutineScope(IO).launch {

            try {
                val newFavoriteCitiesList: MutableList<FavoriteCity> = mutableListOf()

                dao.getAlltoList().forEach {

                    val response =
                        OpenWeatherMapAPIService.openWeatherMapAPIService.getCurrentWeather(
                            it.lat,
                            it.lng,
                            units = UserConfigSharedPreferences.getTemperatureMeasurements()
                        )

                    val city = FavoriteCity(
                        it.name,
                        response.dt!!,
                        response.main!!.temp,
                        response.weather!![0]!!.icon!!,
                        response.main!!.humidity,
                        response.wind!!.speed,
                        response.wind.deg,
                        response.main!!.pressure,
                        it.adminName1,
                        it.lat,
                        it.lng,
                        it.countryName
                    )

                    newFavoriteCitiesList.add(city)
                }

                dao.deleteAll()

                newFavoriteCitiesList.forEach {
                    dao.addCity(it)
                }
            } catch (e: Exception) {

                e.printStackTrace()
                withContext(Main) {
                    Toast.makeText(
                        this@MainActivity, getText(R.string.ic_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun showMenu() {
        viewModel.isMenuVisible.set(true)
        invalidateOptionsMenu()
    }

    fun hideMenu() {
        viewModel.isMenuVisible.set(false)
        invalidateOptionsMenu()
    }
}
