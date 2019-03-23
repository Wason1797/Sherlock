package com.clubsoftware.sherlockbot.HardwareControllers

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.gps.NmeaGpsDriver
import com.google.firebase.database.DatabaseReference
import java.io.IOException
import java.text.ParseException

class GPSController(
    context: Context,
    port: String,
    baud_rate: Int,
    accuracy: Float,
    _TAG: String
) {
    private var mGpsDriver: NmeaGpsDriver? = null
    private var mLocationManager: LocationManager? = null
    private var TAG: String? = null

    public fun registerGPSDriver(mLocationListener: LocationListener) {
        try {
            mGpsDriver!!.register()

            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                900,
                1.5f,
                mLocationListener
            )
        } catch (e: IOException) {
            Log.v(TAG, "Could not configure gps driver")
        }
    }

    fun closeDriver() {
        mGpsDriver!!.unregister()
        try {
            mGpsDriver!!.close()
        } catch (e: IOException) {
        }
    }



    init {
        mLocationManager = context.getSystemService(Activity.LOCATION_SERVICE) as LocationManager
        mGpsDriver = NmeaGpsDriver(
            context,
            port,
            baud_rate,
            accuracy
        )
        TAG = _TAG
    }
}