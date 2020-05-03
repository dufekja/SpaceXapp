package com.dufekja.spacexapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.dufekja.spacexapp.models.Launch
import kotlinx.android.synthetic.main.activity_launch_details.*
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class LaunchDetailsActivity : AppCompatActivity() {
    companion object {
        const val TAG = "LaunchDetailsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_details)

        // toolbar back button
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        if (getSupportActionBar() != null){
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })

        val extras = intent.extras
        if (extras != null) {
            val launch: Launch = extras.get("LAUNCH_TRANSFER") as Launch

            details_missionName.text = launch.missionName
            details_flightNumber.text = "Flight number: ${launch.flightNumber}"
            details_launchSite.text = "Launch site: ${launch.launchSiteName}"

            val dateFormat = SimpleDateFormat("dd. MM. yyyy HH:mm")
            val date = dateFormat.format(Date(Timestamp(launch.launchDateUnix.toLong() * 1000).getTime()))
            details_launchDate.text = "Launch date: $date"

            details_rocketName.text = "Rocket name: ${launch.rocketName}"

            val details = launch.flightDetails
            if (details != "null") details_flightDetails.text = details else details_flightDetails.text = "No details"



            Log.d(TAG, "onCreate: ${launch.missionName}")
        }

        Log.d(TAG, "onCreate: ends")
    }
}
