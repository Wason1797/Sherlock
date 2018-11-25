package com.example.wason.sherlockbot

import android.app.Activity
import android.content.Context
import android.location.*
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.gps.NmeaGpsDriver
import java.io.IOException
import java.text.ParseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GpsTaskBuilder(
    context: Context,
    port: String,
    baud_rate: Int,
    accuracy: Float,
    _TAG: String,
    _dbReference: DatabaseReference
) {
    private var mGpsDriver: NmeaGpsDriver? = null
    private var mLocationManager: LocationManager? = null
    private var TAG: String? = null
    private var dbReference: DatabaseReference? = null

    fun registerGPSDriver() {
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

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            try {
                val msg = "${location.latitude};${location.longitude};speed:${location.speed}"
                Log.v(TAG, msg)
                dbReference!!.child("gps_coordinate").setValue(msg)

            } catch (e: ParseException) {
                Log.v(TAG, e.message)
            }

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.v(TAG, "Status changed, provider: $provider , status: $status")
        }

        override fun onProviderEnabled(provider: String) {
            Log.v(TAG, "Provider enabled: $provider")
        }

        override fun onProviderDisabled(provider: String) {
            Log.v(TAG, "Provider disabled: $provider")
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
        dbReference = _dbReference
    }
}