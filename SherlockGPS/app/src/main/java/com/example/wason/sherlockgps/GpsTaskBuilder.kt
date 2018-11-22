package com.example.wason.sherlockgps

import android.app.Activity
import android.content.Context
import android.location.*
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.gps.NmeaGpsDriver
import java.io.IOException
import java.text.ParseException

class GpsTaskBuilder(context: Context, port: String, baud_rate: Int, accuracy: Float, _TAG: String)
{
    private var mGpsDriver: NmeaGpsDriver? = null
    private var mLocationManager: LocationManager? = null
    private var TAG: String? = null

    fun registerGPSDriver()
    {
        try
        {
            mGpsDriver!!.register()

            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                500,
                1f,
                mLocationListener
            )
        } catch (e: IOException)
        {
            Log.v(TAG, "Could not configure gps driver")
        }
    }

    fun closeDriver()
    {
        mGpsDriver!!.unregister()
        try
        {
            mGpsDriver!!.close()
        } catch (e: IOException)
        {
        }
    }

    private val mLocationListener = object : LocationListener
    {
        override fun onLocationChanged(location: Location)
        {
            try
            {
                Log.v(TAG, "Location update: $location")

            } catch (e: ParseException)
            {
                Log.v(TAG, e.message)
            }

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle)
        {
            Log.v(TAG, "Status changed, provider: $provider , status: $status")
        }

        override fun onProviderEnabled(provider: String)
        {
            Log.v(TAG, "Provider enabled: $provider")
        }

        override fun onProviderDisabled(provider: String)
        {
            Log.v(TAG, "Provider disabled: $provider")
        }
    }

    init
    {
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