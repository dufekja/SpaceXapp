package com.dufekja.spacexapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.dufekja.spacexapp.adapters.LaunchesRecyclerViewAdapter
import com.dufekja.spacexapp.data.DownloadStatus
import com.dufekja.spacexapp.data.GetRawData
import com.dufekja.spacexapp.data.launches.GetSpaceXLaunchesJson
import com.dufekja.spacexapp.listeners.RecyclerItemClickListener
import com.dufekja.spacexapp.models.Launch
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    GetRawData.OnDownloadComplete,
    GetSpaceXLaunchesJson.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {

    companion object {
        private const val TAG = "MainActivity"
        const val LAUNCH_ID = "launchID"

        const val SHARED_PREFS = "SharedPrefsLaunchesCache"
        const val LAUNCHES_LIST_KEY = "LaunchesListKey"
    }

    private val launchesRecyclerViewAdapter = LaunchesRecyclerViewAdapter(ArrayList())

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var url: String
    private lateinit var launchId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extras = intent.extras
        if (extras != null) {
            val msg = extras.getString(LAUNCH_ID)
            launchId = if (msg.isNullOrEmpty()) "" else msg
        } else {
            launchId = ""

        }


        Log.d(TAG, "onCreate: Launch id: $launchId")

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.adapter = launchesRecyclerViewAdapter

        try {
            getLaunchesData("", launchId)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "onCreate with error ${e.message}")
        }


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(findViewById(R.id.toolbar))

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        Log.d(TAG, "onCreate: end")
    }


    private fun createUrl(section: String, param: String, id: String?): String {

        if (!id.isNullOrEmpty()) {
            return "https://api.spacexdata.com/v3/$section/$id"
        }

        return "https://api.spacexdata.com/v3/$section/$param"
    }

    private fun saveData(json: String) {
        Log.d(TAG, "saveData: called")

        val sharedPrefs: SharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        editor.putString(LAUNCHES_LIST_KEY, json)
        editor.apply()

        Log.d(TAG, "saveData: ends")
    }

    private fun loadData(): String? {
        Log.d(TAG, "loadData: called")
        val sharedPrefs: SharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        return sharedPrefs.getString(LAUNCHES_LIST_KEY, null)
    }

    private fun getLaunchesData(param: String, tag: String?) {

        url = createUrl("launches", param, tag)
        val getRawData = GetRawData(this)
        getRawData.execute(url, loadData())
    }

    // listener on menu items
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            // spaceX info
            R.id.nav_info -> {
                startActivity(Intent(this, InfoActivity::class.java))
            }

            // launches
            R.id.nav_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }

            R.id.nav_latest -> {
                getLaunchesData("latest",null)
            }
            R.id.nav_next -> {
                getLaunchesData("next",null)
            }

            R.id.nav_all -> {
                getLaunchesData("", null)
            }
            R.id.nav_upcoming -> {
                getLaunchesData("upcoming",null)
            }
            R.id.nav_past -> {
                getLaunchesData("past",null)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: starts")

        val launch = launchesRecyclerViewAdapter.getLaunch(position)

        if (launch != null) {
            val intent = Intent(this, LaunchDetailsActivity::class.java)
            intent.putExtra("LAUNCH_TRANSFER", launch)
            startActivity(intent)
        }

    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, ".onItemLongClick: starts")

        val launch = launchesRecyclerViewAdapter.getLaunch(position)
        if (launch != null) {
            val show = if (launch.flightDetails == "null") "No details" else launch.flightDetails
            Toast.makeText(this, "$show", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: called with $data")

            saveData(data)

            val getSpaceXLaunchesJson = GetSpaceXLaunchesJson(this)
            getSpaceXLaunchesJson.execute(data)

        } else if (status == DownloadStatus.CACHED) {
            Log.d(TAG, "onDownloadComplete: called with cached data")

            val getSpaceXLaunchesJson = GetSpaceXLaunchesJson(this)
            getSpaceXLaunchesJson.execute(loadData())

        } else {
            Toast.makeText(this, "No launches found", Toast.LENGTH_LONG).show()

            Log.d(TAG, "onDownloadComplete: failed with status $status. Error message is: $data")
        }
    }

    override fun onDataAvailable(data: List<Launch>) {
        Log.d(TAG, "onDataAvailable: called")

        launchesRecyclerViewAdapter.loadNewData(data)

        if (launchesRecyclerViewAdapter.itemCount == 1) {
            val launch = launchesRecyclerViewAdapter.getLaunch(0)

            val intent = Intent(this, LaunchDetailsActivity::class.java)
            intent.putExtra("LAUNCH_TRANSFER", launch)
            startActivity(intent)
        }

        Log.d(TAG, "onDataAvailable: end")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onDataAvailable: With error ${exception.message}")
    }
}