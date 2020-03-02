package com.schaaf.foxweather.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.schaaf.foxweather.R
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.app.App
import com.schaaf.foxweather.databinding.FragmentWeatherInfoBinding
import com.schaaf.foxweather.databinding.RecyclerItemRowForecastDataBinding
import com.schaaf.foxweather.databinding.RecyclerItemRowForecastDateRowBinding
import com.schaaf.foxweather.model.*
import com.schaaf.foxweather.model.Constants.ADMIN_NAME
import com.schaaf.foxweather.model.api_services.OpenWeatherMapAPIService
import com.schaaf.foxweather.model.Constants.CITY_NAME
import com.schaaf.foxweather.model.Constants.COUNTRY_NAME
import com.schaaf.foxweather.model.Constants.LAT
import com.schaaf.foxweather.model.Constants.LNG
import com.schaaf.foxweather.model.pojo.*
import com.schaaf.foxweather.model.room.DataBaseDao
import com.schaaf.foxweather.view_models.MainActivityViewModel
import com.schaaf.foxweather.view_models.WeatherInfoFragmentViewModel
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class WeatherInfoFragment : Fragment() {

    @Inject lateinit var activityViewModel: MainActivityViewModel
    @Inject lateinit var viewModel: WeatherInfoFragmentViewModel
    @Inject lateinit var binding: FragmentWeatherInfoBinding
    @Inject lateinit var activity: MainActivity
    @Inject lateinit var dao: DataBaseDao
    @Inject lateinit var ctx: Context

    private lateinit var forecastViewContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        App.componentsHolder.getWeatherInfoFragmentComponent(this, inflater, container)
            ?.injectsFragment(this)

        binding.vmodel = viewModel
        binding.constants = Constants

        forecastViewContainer = binding.root.findViewById(R.id.forecast_view_layout)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setBottomNavItemChecked()

        if (!activityViewModel.isMenuVisible.get()) activity.showMenu()

        when {

            UserConfigSharedPreferences.getSavedCurrentWeatherData() == null ->
                setUpFragmentWithDefaultCity()

            UserConfigSharedPreferences.getSavedCurrentWeatherData() != null &&
                    viewModel.city!!.get() == "N/A" ->
                setUpFragmentWithLastSavedCity()
        }
    }

    override fun onResume() {
        super.onResume()

        activityViewModel.currentCityDataForAPI.observe(this, Observer { bundle ->

            loadAndBindDataFromServer(bundle)
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        App.componentsHolder.deleteWeatherInfoFragmentComponent()
    }


    //Functions for data manipulation//////////////////////////////////////////////////////////////

    private fun bindDataFromSharedPreferences() {

        val currentWeatherData = UserConfigSharedPreferences.getSavedCurrentWeatherData()
        bindCurrentWeather(currentWeatherData!!)

        val forecastData = UserConfigSharedPreferences.getSavedForecastData()
        bindForecast(forecastData!!)
    }

    private fun saveDataToSharedPreferences(
        currentWeather: CurrentWeatherPOJO,
        forecast: ForecastPOJO
    ) {

        UserConfigSharedPreferences.saveCurrentWeatherData(currentWeather)
        UserConfigSharedPreferences.saveForecastData(forecast)
    }


    private suspend fun loadCurrentWeatherDataFromServer(bundle: Bundle): CurrentWeatherPOJO {

        val lat = bundle.getDouble(LAT)
        val lng = bundle.getDouble(LNG)
        val cityNameFromGeoNamesService = bundle.getString(CITY_NAME)
        val adminName = bundle.getString(ADMIN_NAME)
        val countryName = bundle.getString(COUNTRY_NAME)

        val currentWeatherStateResponse: CurrentWeatherPOJO =
            OpenWeatherMapAPIService.openWeatherMapAPIService.getCurrentWeather(
                lat,
                lng,
                units = UserConfigSharedPreferences.getTemperatureMeasurements()
            )

        currentWeatherStateResponse.nameFromGeoNames = cityNameFromGeoNamesService
        currentWeatherStateResponse.adminName = adminName
        currentWeatherStateResponse.countryName = countryName

        return currentWeatherStateResponse
    }


    private suspend fun loadForecastDataFromServer(bundle: Bundle): ForecastPOJO {

        val lat = bundle.getDouble(LAT)
        val lng = bundle.getDouble(LNG)

        return OpenWeatherMapAPIService.openWeatherMapAPIService.getForecast(
            lat,
            lng,
            units = UserConfigSharedPreferences.getTemperatureMeasurements()
        )
    }


    private fun bindCurrentWeather(data: CurrentWeatherPOJO) {

        viewModel.city!!.set(data.nameFromGeoNames)
        viewModel.date!!.set(data.dt!!)
        viewModel.icon?.set(data.weather!![0]!!.icon)
        viewModel.humidity?.set(data.main!!.humidity)
        viewModel.pressure?.set(data.main!!.pressure)
        viewModel.windSpeed?.set(data.wind!!.speed)
        viewModel.windDegree?.set(data.wind!!.deg)
        viewModel.temp!!.set(data.main!!.temp)

        val regionBundle = Bundle()
        regionBundle.putString(ADMIN_NAME,data.adminName)
        regionBundle.putString(COUNTRY_NAME,data.countryName)

        viewModel.region!!.set(regionBundle)
    }


    private fun bindForecast(data: ForecastPOJO) {

        CoroutineScope(Main).launch {

            val dataForAdapter = getForecastListWithSeparators(data.list)

            viewModel.forecastDataList = dataForAdapter

            forecastViewContainer.removeAllViews()


            //Creating TreeView with "bmelnychuk/AndroidTreeView" library

            val root = TreeNode.root()

            val parentsTreeNodesList: MutableList<TreeNode> = mutableListOf()

            val listSeparators: MutableList<ListSeparator> = mutableListOf()

            dataForAdapter.forEach { forecastRWItem ->

                if (forecastRWItem is ListSeparator) {

                    val parent = TreeNode(forecastRWItem)
                    parent.viewHolder = ListSeparatorHolder()
                    parentsTreeNodesList.add(parent)
                    listSeparators.add(forecastRWItem)
                }
            }

            dataForAdapter.forEach {

                if (it is ListItem) {

                    when (getFormattedDate(it.getDate())) {

                        getFormattedDate(listSeparators[0].getDate()) -> {
                            val child = TreeNode(it)
                            child.viewHolder = ListItemHolder()
                            parentsTreeNodesList[0].addChildren(child)
                        }

                        getFormattedDate(listSeparators[1].getDate()) -> {
                            val child = TreeNode(it)
                            child.viewHolder = ListItemHolder()
                            parentsTreeNodesList[1].addChildren(child)
                        }

                        getFormattedDate(listSeparators[2].getDate()) -> {
                            val child = TreeNode(it)
                            child.viewHolder = ListItemHolder()
                            parentsTreeNodesList[2].addChildren(child)
                        }

                        getFormattedDate(listSeparators[3].getDate()) -> {
                            val child = TreeNode(it)
                            child.viewHolder = ListItemHolder()
                            parentsTreeNodesList[3].addChildren(child)
                        }

                        getFormattedDate(listSeparators[4].getDate()) -> {
                            val child = TreeNode(it)
                            child.viewHolder = ListItemHolder()
                            parentsTreeNodesList[4].addChildren(child)
                        }

                        getFormattedDate(listSeparators[5].getDate()) -> {
                            val child = TreeNode(it)
                            child.viewHolder = ListItemHolder()
                            parentsTreeNodesList[5].addChildren(child)
                        }
                    }
                }

            }

            parentsTreeNodesList.forEach {
                root.addChild(it)
            }

            val treeView = AndroidTreeView(activity, root)
            treeView.setDefaultAnimation(true)

            treeView.setDefaultNodeClickListener { node, _ ->

                fun backgroundBlackout(toAlpha: Float, iView: ImageView) {

                    iView.animate().apply {
                        interpolator = LinearInterpolator()
                        duration = 350
                        alpha(toAlpha)
                        start()
                    }
                }

                val checkBox: CheckBox = node.viewHolder.view.findViewById(R.id.checkBox)
                checkBox.isChecked = !checkBox.isChecked

                val imView = node.viewHolder.view.findViewById<ImageView>(R.id.f_background)

                if (node.isExpanded) {
                    backgroundBlackout(0.0f, imView)
                } else {
                    backgroundBlackout(0.5f, imView)
                }

            }

            forecastViewContainer.removeAllViews()

            forecastViewContainer.addView(treeView.view)
        }
    }


    private fun loadAndBindDataFromServer(bundle: Bundle) {

        activityViewModel.isLoadAndBindProgressBarVisible.set(true)

        activity.hideMenu()

        CoroutineScope(IO).launch {

            try {
                val currentWeatherDataFromServer = loadCurrentWeatherDataFromServer(bundle)
                val forecastDataFromServer = loadForecastDataFromServer(bundle)

                withContext(Main) {

                    bindCurrentWeather(currentWeatherDataFromServer)
                    bindForecast(forecastDataFromServer)
                }
                saveDataToSharedPreferences(currentWeatherDataFromServer, forecastDataFromServer)
            } catch (E: Exception) {

                if (UserConfigSharedPreferences.getSavedCurrentWeatherData() != null &&
                    UserConfigSharedPreferences.getSavedForecastData() != null
                ) {

                    withContext(Main) {
                        Toast.makeText(
                            ctx,
                            ctx.getText(R.string.ic_error2),
                            Toast.LENGTH_LONG
                        ).show()
                        bindDataFromSharedPreferences()
                    }

                } else withContext(Main) {
                    Toast.makeText(ctx, ctx.getText(R.string.ic_error), Toast.LENGTH_LONG).show()
                }
            }finally {

                if (activityViewModel.isLoadAndBindProgressBarVisible.get())
                    activityViewModel.isLoadAndBindProgressBarVisible.set(false)

                activity.showMenu()
            }
        }
    }

    //Util functions///////////////////////////////////////////////////////////////////////////////

    private fun setUpFragmentWithDefaultCity(){

        val bundle = Bundle()
        bundle.putDouble(LAT, 59.93863)
        bundle.putDouble(LNG, 30.31413)

        val name = when (Locale.getDefault().toLanguageTag()) {

            "en-US" -> "Saint Petersburg"
            "ru-RU" -> "Санкт Петербург"
            else -> "Saint Petersburg"
        }

        bundle.putString(CITY_NAME, name)
        bundle.putString(ADMIN_NAME, name)

        val countryName = when (Locale.getDefault().toLanguageTag()) {

            "en-US" -> "Russia"
            "ru-RU" -> "Россия"
            else -> "Russia"
        }

        bundle.putString(COUNTRY_NAME, countryName)

        activityViewModel.currentCityDataForAPI.value = bundle
    }

    private fun setUpFragmentWithLastSavedCity(){

        val currentWeatherDataFromSharedPreferences =
            UserConfigSharedPreferences.getSavedCurrentWeatherData()

        val forecastDataFromSharedPreferences =
            UserConfigSharedPreferences.getSavedForecastData()

        if (currentWeatherDataFromSharedPreferences != null && forecastDataFromSharedPreferences != null) {

            val bundle = Bundle()
            bundle.putDouble(LAT, currentWeatherDataFromSharedPreferences.coord!!.lat!!)
            bundle.putDouble(LNG, currentWeatherDataFromSharedPreferences.coord.lon!!)
            bundle.putString(
                CITY_NAME,
                currentWeatherDataFromSharedPreferences.nameFromGeoNames!!
            )
            bundle.putString(ADMIN_NAME, currentWeatherDataFromSharedPreferences.adminName)
            bundle.putString(COUNTRY_NAME, currentWeatherDataFromSharedPreferences.countryName)

            activityViewModel.currentCityDataForAPI.value = bundle
        }

    }

    private fun setBottomNavItemChecked() {

        activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .menu
            .getItem(0)
            .isChecked = true
    }

    private fun getFormattedDate(unix_date: Int): String {

        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH)
        val date = Date(unix_date.toLong() * 1000)
        return dateFormat.format(date)
    }

    private fun getForecastListWithSeparators(input: List<ListItem>?): List<ForecastRecyclerViewItem> {

        val newList: MutableList<ForecastRecyclerViewItem> = mutableListOf()

        input!!.forEachIndexed { index, listItem ->

            if (index == 0) {
                newList.add(0, ListSeparator(listItem.dt))
                newList.add(1, listItem)

            } else {

                if (getFormattedDate(listItem.dt) != getFormattedDate(newList[newList.size - 1].getDate())) {
                    newList.add(ListSeparator(listItem.dt))
                }
                newList.add(listItem)
            }
        }

        return newList
    }


    inner class ListItemHolder :
        TreeNode.BaseNodeViewHolder<ListItem>(activity.applicationContext) {

        override fun createNodeView(node: TreeNode?, value: ListItem?): View {

            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val binding =
                DataBindingUtil.inflate<RecyclerItemRowForecastDataBinding>(
                    inflater,
                    R.layout.recycler_item_row_forecast_data,
                    null,
                    false
                )
            binding.listItem = value
            binding.constants = Constants
            return binding.root
        }
    }

    inner class ListSeparatorHolder :
        TreeNode.BaseNodeViewHolder<ListSeparator>(activity.applicationContext) {

        override fun createNodeView(node: TreeNode?, value: ListSeparator?): View {

            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val binding = DataBindingUtil.inflate<RecyclerItemRowForecastDateRowBinding>(
                inflater,
                R.layout.recycler_item_row_forecast_date_row,
                null,
                false
            )
            binding.item = value
            return binding.root
        }
    }

}
