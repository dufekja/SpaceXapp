package com.dufekja.spacexapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    companion object {
        const val TAG = "SearchActivity"
        const val LAUNCH_ID = "launchID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // toolbar and back button
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

        // search button listener
        search_button.setOnClickListener {
            val message = search_flightNumber.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(LAUNCH_ID, message)
            startActivity(intent)
        }

        Log.d(TAG, "onCreate: ends")
    }
}
