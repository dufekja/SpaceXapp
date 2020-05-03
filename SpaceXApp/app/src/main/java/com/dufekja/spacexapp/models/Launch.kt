package com.dufekja.spacexapp.models

import android.util.Log
import java.io.IOException
import java.io.ObjectStreamException
import java.io.Serializable
import java.sql.Timestamp

class Launch(var flightNumber: String, var missionName: String,  var launchDateUnix: String, var rocketName: String,
    var flightDetails: String, var launchSiteName: String)
    : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    override fun toString(): String {
        return "Launch(" +
                "flightNumber='$flightNumber', " +
                "missionName='$missionName', " +
                "launchDateUnix='$launchDateUnix', " +
                "rocketName='$rocketName', " +
                "flightDetails='$flightDetails', " +
                "launchSiteName='$launchSiteName')"
    }

    @Throws(IOException::class)
    private fun writeObject(out: java.io.ObjectOutputStream) {
        Log.d("Launch", "writeObj called")
        out.writeUTF(flightNumber)
        out.writeUTF(missionName)
        out.writeUTF(launchDateUnix)
        out.writeUTF(rocketName)
        out.writeUTF(flightDetails)
        out.writeUTF(launchSiteName)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inStream: java.io.ObjectInputStream) {
        Log.d("Launch", "readObj called")
        flightNumber = inStream.readUTF()
        missionName = inStream.readUTF()
        launchDateUnix = inStream.readUTF()
        rocketName = inStream.readUTF()
        flightDetails = inStream.readUTF()
        launchSiteName = inStream.readUTF()
    }

    @Throws(ObjectStreamException::class)
    private fun readObjectNoData() {
        Log.d("Launch", "readObjectNoData called")
    }

}