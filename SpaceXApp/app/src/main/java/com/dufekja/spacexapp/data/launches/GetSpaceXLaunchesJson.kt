package com.dufekja.spacexapp.data.launches

import android.os.AsyncTask
import android.util.Log
import com.dufekja.spacexapp.models.Launch
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray as JSONArray

class GetSpaceXLaunchesJson(private val listener: OnDataAvailable) : AsyncTask<String, Void, ArrayList<Launch>>() {
    companion object {
        private val TAG = "GetSpaceXLaunchesJson"
    }

    interface OnDataAvailable {
        fun onDataAvailable(data: List<Launch>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String): ArrayList<Launch> {
        Log.d(TAG, "doInBackground: starts")

        val launchesList = ArrayList<Launch>()

        try {
            val launches = JSONArray(params[0])

            // loop trough all launches and get theirs values
            for (i in 0 until launches.length()) {

                val launch = launches.getJSONObject(i)

                val launchObj = createLaunchJsonObj(launch)
                launchesList.add(launchObj)

            }

            Log.d(TAG, "doInBackground: loop ends")
        } catch (e: JSONException) {

            val launch = JSONObject(params[0])
            launchesList.add(createLaunchJsonObj(launch))

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "doInBackground: Error processing Json data. ${e.message}")
            cancel(true)
            listener.onError(e)
        }

        Log.d(TAG, "doInBackground: ends")
        return launchesList
    }

    override fun onPostExecute(result: ArrayList<Launch>) {
        Log.d(TAG, "onPostExecute: starts")
        super.onPostExecute(result)
        listener.onDataAvailable(result)
        Log.d(TAG, "onPostExecute: ends")

    }

    private fun createLaunchJsonObj(launch: JSONObject): Launch {

        val flightNumber = launch.getString("flight_number")
        val missionName = launch.getString("mission_name")
        val launchDateUnix = launch.getString("launch_date_unix")

        val rocket = launch.getJSONObject("rocket")
        val rocketName = rocket.getString("rocket_name")
        val flightDetails = launch.getString("details")

        val site = launch.getJSONObject("launch_site")
        val siteName = site.getString("site_name")

        val launchObj = Launch(flightNumber, missionName, launchDateUnix, rocketName, flightDetails, siteName)

        Log.d(TAG, "createLaunchJsonObj: obj > $launchObj")
        return launchObj
    }
}