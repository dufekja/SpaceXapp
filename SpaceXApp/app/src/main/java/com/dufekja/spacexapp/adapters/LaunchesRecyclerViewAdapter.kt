package com.dufekja.spacexapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dufekja.spacexapp.R
import com.dufekja.spacexapp.models.Launch
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class LaunchItemViewHold(view: View): RecyclerView.ViewHolder(view) {
    var missionName: TextView = view.findViewById(R.id.item_missionName)
    var flightNumber: TextView = view.findViewById(R.id.item_flightNumber)
    var launchDate: TextView = view.findViewById(R.id.item_launchDate)
}

class LaunchesRecyclerViewAdapter(private var launchesList: List<Launch>): RecyclerView.Adapter<LaunchItemViewHold>() {
    companion object {
        const val TAG = "LaunchRecyclerAdapt"
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: called")
        return if(launchesList.isNotEmpty()) launchesList.size else 0
    }

    fun loadNewData(newLaunches: List<Launch>) {
        launchesList = newLaunches
        notifyDataSetChanged()
    }

    fun getLaunch(position: Int): Launch? {
        return if (launchesList.isNotEmpty()) launchesList[position] else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchItemViewHold {
        Log.d(TAG, "onCreateViewHolder: new view request")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_item, parent, false)
        return LaunchItemViewHold(view)
    }

    override fun onBindViewHolder(holder: LaunchItemViewHold, position: Int) {

        val launchItem = launchesList[position]

        val name = launchItem.missionName
        holder.missionName.text = if (name.length > 20) name.take(20) + "..." else name
        holder.flightNumber.text = "Launch number: " + launchItem.flightNumber

        val dateFormat = SimpleDateFormat("dd. MM. yyyy HH:mm")
        val date = dateFormat.format(Date(Timestamp(launchItem.launchDateUnix.toLong() * 1000).getTime()))
        holder.launchDate.text = date

    }
}