package com.dufekja.spacexapp.data.launches

import android.os.AsyncTask
import android.util.Log
import com.dufekja.spacexapp.models.Info
import org.json.JSONObject

class GetSpaceXInfoJson(private val listener: OnInfoAvailable) : AsyncTask<String, Void, Info>() {
    companion object {
        private val TAG = "GetSpaceXInfoJson"
    }

    interface OnInfoAvailable {
        fun onInfoAvailable(data: Info)
    }

    override fun doInBackground(vararg params: String): Info {
        Log.d(TAG, "doInBackground: starts")


        try {
            val info =  createInfoJsonObj(JSONObject(params[0]))
            return info

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "doInBackground: Error processing Json data. ${e.message}")
            cancel(true)
        }

        Log.d(TAG, "doInBackground: ends")
        return Info("","","","","")
    }

    override fun onPostExecute(result: Info) {
        Log.d(TAG, "onPostExecute: starts")
        super.onPostExecute(result)
        listener.onInfoAvailable(result)
        Log.d(TAG, "onPostExecute: ends")

    }

    private fun createInfoJsonObj(data: JSONObject): Info {

        val name = data.getString("name")
        val founder = data.getString("founder")
        val founded = data.getString("founded")
        val employees = data.getString("employees")
        val valuation = data.getString("valuation")


        return Info(name, founder, founded, employees, valuation)
    }
}