package com.dufekja.spacexapp



import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dufekja.spacexapp.data.DownloadStatus
import com.dufekja.spacexapp.data.GetRawData
import com.dufekja.spacexapp.data.launches.GetSpaceXInfoJson
import com.dufekja.spacexapp.models.Info
import kotlinx.android.synthetic.main.activity_info.*


class InfoActivity : AppCompatActivity(),
    GetSpaceXInfoJson.OnInfoAvailable,
    GetRawData.OnDownloadComplete {

    companion object {
        const val TAG = "InfoActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

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

        // get url string json
        val url = "https://api.spacexdata.com/v3/info"
        val getRawData = GetRawData(this)
        getRawData.execute(url, null)

        Log.d(TAG, "onCreate: ends")
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: called with $data")

            val getSpaceXInfoJson = GetSpaceXInfoJson(this)
            getSpaceXInfoJson.execute(data)

        } else {
            // download failed

            Toast.makeText(this, "No connection to internet", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "onDownloadComplete: failed with status $status. Error message is: $data")
        }
    }

    override fun onInfoAvailable(data: Info) {
        Log.d(TAG, "onInfoAvailable: called")

        info_name.text = "Company name: ${data.name}"
        info_founder.text = "Founder name: ${data.founder}"
        info_founded.text = "Founded in: ${data.founded}"
        info_employees.text = "Number of employees: ${data.employees}"
        info_valuation.text = "${data.name} valuation: $${data.valuation}"

        Log.d(TAG, "onInfoAvailable: ends")
    }
}
