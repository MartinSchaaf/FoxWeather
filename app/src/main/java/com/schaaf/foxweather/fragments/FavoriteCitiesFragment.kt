package com.schaaf.foxweather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.schaaf.foxweather.R
import com.schaaf.foxweather.activity.MainActivity
import com.schaaf.foxweather.app.App
import com.schaaf.foxweather.dagger.components.FavoriteCitiesFragmentComponent
import com.schaaf.foxweather.databinding.FragmentFavoriteCitiesBinding
import com.schaaf.foxweather.model.favorite_cities_list.FavoriteCityRecyclerViewAdapter
import com.schaaf.foxweather.model.room.DataBaseDao
import com.schaaf.foxweather.view_models.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject


class FavoriteCitiesFragment : Fragment() {

    @Inject lateinit var biding: FragmentFavoriteCitiesBinding
    @Inject lateinit var activityViewModel: MainActivityViewModel
    @Inject lateinit var adapter: FavoriteCityRecyclerViewAdapter
    @Inject lateinit var dao: DataBaseDao
    @Inject lateinit var activity:MainActivity
    lateinit var fab:FloatingActionButton

    private lateinit var recyclerView: RecyclerView
    private lateinit var noCitiesHereText:TextView
    lateinit var daggerComponent: FavoriteCitiesFragmentComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        daggerComponent = App.componentsHolder.getFavoriteCitiesFragmentComponent(this,inflater, container)!!
        daggerComponent.injectsFavoriteCitiesFragment(this)

        recyclerView = biding.root.findViewById(R.id.favorite_cities_recycler_view)
        noCitiesHereText = biding.root.findViewById(R.id.no_city_here)
        fab = biding.root.findViewById(R.id.fab)

        return biding.root
    }

    override fun onStart() {
        super.onStart()

        setBottomNavItemChecked()

        biding.fragment = this

        if (!activityViewModel.isMenuVisible.get()) activity.showMenu()

        CoroutineScope(Main).launch {

            recyclerView.layoutManager = LinearLayoutManager(activity.applicationContext)
            recyclerView.adapter = adapter

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        if (fab.isShown) fab.hide()

                    } else if (dy < 0) {
                        if (!fab.isShown) fab.show()
                    }
                }
            })

            dao.getAlltoLiveData().observe(this@FavoriteCitiesFragment, Observer {

                adapter.updateAdapter(it)
                invalidateRecyclerViewVisibility()
                if(adapter.data?.size!! <= 2 && !fab.isShown) fab.show()
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isRemoving) App.componentsHolder.deleteFavoriteCitiesFragmentComponent()
    }

    fun onFloatingActionButtonClick() {
        activity.findViewById<MaterialSearchView>(R.id.search_view)?.showSearch()
    }

    private fun setBottomNavItemChecked() {

        activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .menu
            .getItem(1)
            .isChecked = true
    }

    private fun invalidateRecyclerViewVisibility(){
        noCitiesHereText.visibility = if (adapter.data!!.isEmpty()) View.VISIBLE else View.GONE
    }
}
